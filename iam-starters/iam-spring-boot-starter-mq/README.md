# IAM Spring Boot Starter MQ

IAM平台消息队列功能Starter，基于RocketMQ提供企业级消息传递能力。

## 功能特性

### 🚀 核心功能

- **RocketMQ集成**: 完整的RocketMQ支持，包括生产者和消费者配置
- **事件驱动架构**: 基于领域事件的事件驱动架构支持
- **消息序列化**: 多种序列化方式支持（JSON、Protobuf、Kryo）
- **死信队列处理**: 自动死信队列处理和重试机制
- **消息追踪**: 完整的消息链路追踪和监控
- **性能监控**: 集成Micrometer指标收集

### 📦 包含组件

| 组件 | 功能说明 |
|------|----------|
| **MessageSender** | 统一的消息发送接口 |
| **EventPublisher** | 事件发布器，支持领域事件 |
| **MessageSerializer** | 消息序列化器 |
| **DeadLetterProcessor** | 死信队列处理器 |
| **MessageTraceService** | 消息追踪服务 |
| **MqMonitorService** | 消息队列监控服务 |

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.xiaoxin</groupId>
    <artifactId>iam-spring-boot-starter-mq</artifactId>
</dependency>
```

### 2. 基础配置

```yaml
iam:
  mq:
    enabled: true
    # RocketMQ配置
    rocketmq:
      name-server: localhost:9876
      producer:
        group: iam_producer_group
        send-msg-timeout: 3s
      consumer:
        group: iam_consumer_group
        consume-thread-min: 20
        consume-thread-max: 64
```

### 3. 发送消息

```java
@Service
public class UserService {
    
    @Autowired
    private MessageSender messageSender;
    
    public void createUser(User user) {
        // 业务逻辑
        userRepository.save(user);
        
        // 发送消息
        SendResult result = messageSender.sendSync("user_topic", "created", user);
        if (result.isSuccess()) {
            log.info("User created message sent: {}", result.getMessageId());
        }
    }
}
```

### 4. 事件驱动

```java
// 定义领域事件
public class UserCreatedEvent extends DomainEvent {
    private User user;
    
    public UserCreatedEvent(User user) {
        super(user.getId().toString(), "User");
        this.user = user;
    }
    
    @Override
    public DomainEvent copy() {
        return new UserCreatedEvent(this.user);
    }
}

// 发布事件
@Service
public class UserService {
    
    @Autowired
    private EventPublisher eventPublisher;
    
    public void createUser(User user) {
        userRepository.save(user);
        
        // 发布领域事件
        eventPublisher.publish(new UserCreatedEvent(user));
    }
}
```

### 5. 消费消息

```java
@Component
@RocketMQMessageListener(
    topic = "user_topic",
    consumerGroup = "user_consumer_group"
)
public class UserMessageListener implements RocketMQListener<User> {
    
    @Override
    public void onMessage(User user) {
        log.info("Received user message: {}", user.getId());
        // 处理消息
    }
}
```

## 详细配置

### RocketMQ配置

```yaml
iam:
  mq:
    rocketmq:
      name-server: localhost:9876
      # 生产者配置
      producer:
        group: iam_producer_group
        send-msg-timeout: 3s
        compress-msg-body-over-how-much: 4096
        retry-times-when-send-failed: 2
        retry-times-when-send-async-failed: 2
        max-message-size: 4194304
        vip-channel-enabled: false
      
      # 消费者配置
      consumer:
        group: iam_consumer_group
        consume-mode: CONCURRENTLY
        consume-from-where: CONSUME_FROM_LAST_OFFSET
        consume-thread-min: 20
        consume-thread-max: 64
        consume-timeout: 15m
        consume-message-batch-max-size: 1
        vip-channel-enabled: false
      
      # 访问控制配置
      acl:
        enabled: false
        access-key: your-access-key
        secret-key: your-secret-key
```

### 事件驱动配置

```yaml
iam:
  mq:
    event:
      enabled: true
      topic-prefix: "iam_event_"
      enable-async: true
      failure-strategy: LOG_AND_IGNORE # LOG_AND_IGNORE, THROW_EXCEPTION, RETRY
      
      # 异步线程池配置
      thread-pool:
        core-size: 5
        max-size: 20
        queue-capacity: 100
        keep-alive: 60s
        thread-name-prefix: "iam-event-"
```

### 序列化配置

```yaml
iam:
  mq:
    serialization:
      type: JSON # JSON, PROTOBUF, KRYO
      enable-compression: false
      compression-threshold: 1024
      
      # JSON序列化配置
      json:
        include-type-info: true
        date-format: "yyyy-MM-dd HH:mm:ss"
        time-zone: "GMT+8"
```

### 死信队列配置

```yaml
iam:
  mq:
    dead-letter:
      enabled: true
      topic-suffix: "_DLQ"
      max-retry-times: 16
      process-strategy: MANUAL # MANUAL, AUTO_RETRY, AUTO_DISCARD
      auto-process-interval: 1h
      retention-time: 7d
```

### 消息追踪配置

```yaml
iam:
  mq:
    trace:
      enabled: true
      topic: "RMQ_SYS_TRACE_TOPIC"
      group: "iam_trace_group"
      max-msg-size: 128000
      enable-metrics: true
      
      # 存储配置
      storage:
        type: MEMORY # MEMORY, DATABASE, ELASTICSEARCH
        memory:
          max-size: 10000
          expire-after-write: 1h
        database:
          table-prefix: "mq_trace_"
          batch-size: 100
          flush-interval: 5s
```

### 重试配置

```yaml
iam:
  mq:
    retry:
      enabled: true
      max-attempts: 3
      interval: 1s
      multiplier: 2.0
      max-interval: 5m
      strategy: EXPONENTIAL_BACKOFF # FIXED_DELAY, EXPONENTIAL_BACKOFF, CUSTOM
```

### 监控配置

```yaml
iam:
  mq:
    monitor:
      enabled: true
      collect-interval: 30s
      enable-jmx: true
      enable-micrometer: true
      custom-metrics:
        business.metric1: "value1"
        business.metric2: "value2"
```

## 使用示例

### 1. 同步发送消息

```java
@Service
public class OrderService {
    
    @Autowired
    private MessageSender messageSender;
    
    public void processOrder(Order order) {
        // 同步发送
        SendResult result = messageSender.sendSync("order_topic", "created", order);
        
        if (result.isSuccess()) {
            log.info("Order message sent successfully: {}", result.getMessageId());
        } else {
            log.error("Failed to send order message: {}", result.getErrorMessage());
        }
    }
}
```

### 2. 异步发送消息

```java
@Service
public class NotificationService {
    
    @Autowired
    private MessageSender messageSender;
    
    public void sendNotification(Notification notification) {
        // 异步发送
        messageSender.sendAsync("notification_topic", notification, new SendCallback() {
            @Override
            public void onSuccess(SendResult result) {
                log.info("Notification sent: {}", result.getMessageId());
            }
            
            @Override
            public void onException(Throwable exception) {
                log.error("Failed to send notification", exception);
            }
        });
    }
}
```

### 3. 发送顺序消息

```java
@Service
public class PaymentService {
    
    @Autowired
    private MessageSender messageSender;
    
    public void processPayment(Payment payment) {
        // 按用户ID保证顺序
        String hashKey = payment.getUserId().toString();
        SendResult result = messageSender.sendOrderly("payment_topic", payment, hashKey);
        
        log.info("Payment message sent orderly: {}", result.getMessageId());
    }
}
```

### 4. 发送延时消息

```java
@Service
public class ScheduleService {
    
    @Autowired
    private MessageSender messageSender;
    
    public void scheduleTask(Task task) {
        // 延时级别: 1=1s, 2=5s, 3=10s, 4=30s, 5=1m, 6=2m, 7=3m, 8=4m, 9=5m, 10=6m, 11=7m, 12=8m, 13=9m, 14=10m, 15=20m, 16=30m, 17=1h, 18=2h
        int delayLevel = 10; // 6分钟后执行
        SendResult result = messageSender.sendDelayMessage("schedule_topic", task, delayLevel);
        
        log.info("Scheduled task message sent: {}", result.getMessageId());
    }
}
```

### 5. 事件发布

```java
// 定义用户注册事件
public class UserRegisteredEvent extends DomainEvent {
    private String userId;
    private String username;
    private String email;
    
    public UserRegisteredEvent(String userId, String username, String email) {
        super(userId, "User");
        this.userId = userId;
        this.username = username;
        this.email = email;
    }
    
    @Override
    public DomainEvent copy() {
        return new UserRegisteredEvent(this.userId, this.username, this.email);
    }
    
    // getters and setters
}

// 发布事件
@Service
public class UserService {
    
    @Autowired
    private EventPublisher eventPublisher;
    
    @Transactional
    public void registerUser(UserRegistrationRequest request) {
        // 创建用户
        User user = new User(request.getUsername(), request.getEmail());
        userRepository.save(user);
        
        // 发布用户注册事件
        UserRegisteredEvent event = new UserRegisteredEvent(
            user.getId().toString(),
            user.getUsername(),
            user.getEmail()
        );
        
        // 同步发布
        eventPublisher.publish(event);
        
        // 或异步发布
        // eventPublisher.publishAsync(event);
    }
}
```

### 6. 事件监听

```java
@Component
@RocketMQMessageListener(
    topic = "iam_event_user_registered_event",
    consumerGroup = "email_notification_consumer"
)
public class UserRegisteredEventListener implements RocketMQListener<UserRegisteredEvent> {
    
    @Autowired
    private EmailService emailService;
    
    @Override
    public void onMessage(UserRegisteredEvent event) {
        log.info("Received user registered event: {}", event.getUserId());
        
        // 发送欢迎邮件
        emailService.sendWelcomeEmail(event.getEmail(), event.getUsername());
    }
}
```

### 7. 批量消费

```java
@Component
@RocketMQMessageListener(
    topic = "batch_topic",
    consumerGroup = "batch_consumer",
    consumeMode = ConsumeMode.CONCURRENTLY
)
public class BatchMessageListener implements RocketMQListener<List<String>> {
    
    @Override
    public void onMessage(List<String> messages) {
        log.info("Received batch messages, count: {}", messages.size());
        
        // 批量处理消息
        for (String message : messages) {
            processMessage(message);
        }
    }
    
    private void processMessage(String message) {
        // 处理单个消息
    }
}
```

## 监控和诊断

### 1. 健康检查

```java
@Component
public class MqHealthIndicator implements HealthIndicator {
    
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    
    @Override
    public Health health() {
        try {
            // 检查连接状态
            // 这里可以发送一个测试消息来验证连接
            return Health.up()
                    .withDetail("rocketmq", "Connected")
                    .build();
        } catch (Exception e) {
            return Health.down()
                    .withDetail("rocketmq", "Disconnected")
                    .withException(e)
                    .build();
        }
    }
}
```

### 2. 性能指标

```java
@Component
public class MqMetrics {
    
    private final MeterRegistry meterRegistry;
    private final Counter messagesSent;
    private final Counter messagesReceived;
    private final Timer sendTimer;
    
    public MqMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.messagesSent = Counter.builder("mq.messages.sent")
                .description("Number of messages sent")
                .register(meterRegistry);
        this.messagesReceived = Counter.builder("mq.messages.received")
                .description("Number of messages received")
                .register(meterRegistry);
        this.sendTimer = Timer.builder("mq.send.duration")
                .description("Message send duration")
                .register(meterRegistry);
    }
    
    public void recordSent() {
        messagesSent.increment();
    }
    
    public void recordReceived() {
        messagesReceived.increment();
    }
    
    public Timer.Sample startSendTimer() {
        return Timer.start(meterRegistry);
    }
}
```

## 最佳实践

### 1. 消息设计

- **幂等性**: 确保消息处理的幂等性
- **版本控制**: 为消息定义版本，支持向后兼容
- **大小限制**: 控制消息大小，避免过大消息影响性能

### 2. 错误处理

- **重试机制**: 合理设置重试次数和间隔
- **死信队列**: 处理无法消费的消息
- **监控告警**: 及时发现和处理异常

### 3. 性能优化

- **批量消费**: 适当使用批量消费提高吞吐量
- **线程池配置**: 根据业务特点合理配置消费者线程池
- **序列化选择**: 根据性能要求选择合适的序列化方式

### 4. 运维监控

- **链路追踪**: 启用消息追踪，便于问题排查
- **指标收集**: 收集关键业务和技术指标
- **日志规范**: 记录关键操作日志

## 故障排查

### 1. 连接问题

```bash
# 检查NameServer连接
telnet nameserver-host 9876

# 查看生产者状态
curl http://localhost:8080/actuator/health/rocketmq
```

### 2. 消息堆积

```bash
# 查看队列状态
sh mqadmin queryMsgByQueue -q 0 -t your-topic -n nameserver:9876

# 查看消费者进度
sh mqadmin consumerProgress -c your-consumer-group -n nameserver:9876
```

### 3. 性能问题

```bash
# 查看消息发送TPS
curl http://localhost:8080/actuator/metrics/mq.messages.sent

# 查看消息处理延迟
curl http://localhost:8080/actuator/metrics/mq.send.duration
```

## 版本兼容性

| Starter版本 | RocketMQ版本 | Spring Boot版本 |
|-------------|--------------|-----------------|
| 1.0.0-SNAPSHOT | 5.3.x | 3.2.x |

## 许可证

本项目采用Apache 2.0许可证，详情请参阅[LICENSE](../../LICENSE)文件。
