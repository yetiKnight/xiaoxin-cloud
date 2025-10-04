# IAM登录前端应用设计文档

## 1. 概述

本文档描述了为IAM平台创建专门的登录前端应用的详细计划。该应用将基于Vue3技术栈构建，用于处理用户认证相关功能，包括登录、注册、密码重置等。

## 2. 项目结构规划

```
apps/
└── iam-login-frontend/
    ├── public/
    │   ├── favicon.ico
    │   └── index.html
    ├── src/
    │   ├── assets/
    │   │   └── logo.png
    │   ├── components/
    │   │   ├── LoginForm.vue
    │   │   ├── RegisterForm.vue
    │   │   └── SocialLogin.vue
    │   ├── views/
    │   │   ├── Login.vue
    │   │   ├── Register.vue
    │   │   └── ResetPassword.vue
    │   ├── router/
    │   │   └── index.js
    │   ├── store/
    │   │   └── index.js
    │   ├── utils/
    │   │   ├── auth.js
    │   │   ├── request.js
    │   │   └── storage.js
    │   ├── styles/
    │   │   └── global.css
    │   ├── App.vue
    │   └── main.js
    ├── package.json
    ├── vite.config.js
    └── README.md
```

## 3. 技术栈选型

### 前端框架
- Vue 3 (Composition API)
- Vue Router 4
- Pinia (状态管理)
- Vite (构建工具)

### UI组件库
- Element Plus (推荐) 或 Naive UI

### HTTP客户端
- Axios

### 样式处理
- Sass/SCSS

### 代码规范
- ESLint
- Prettier

## 4. 功能模块设计

### 核心功能
1. 用户登录
2. 用户注册
3. 密码重置

### 页面设计
1. 登录页面 (/login)
2. 注册页面 (/register)
3. 忘记密码页面 (/forgot-password)
4. 重置密码页面 (/reset-password/:token)

## 5. 开发计划

### 阶段一：环境搭建 (1天)
- [ ] 创建项目目录结构
- [ ] 初始化package.json
- [ ] 配置Vite构建环境
- [ ] 集成Vue Router和Pinia
- [ ] 配置ESLint和Prettier
- [ ] 集成UI组件库

### 阶段二：基础架构搭建 (2天)
- [ ] 实现路由配置
- [ ] 创建公共组件
- [ ] 实现HTTP请求封装
- [ ] 实现认证工具函数
- [ ] 实现本地存储工具

### 阶段三：核心功能开发 (3天)
- [ ] 登录页面开发
- [ ] 注册页面开发
- [ ] 密码重置功能开发
- [ ] 第三方登录集成
- [ ] 表单验证实现

### 阶段四：优化完善 (2天)
- [ ] 响应式设计优化
- [ ] 错误处理完善
- [ ] 用户体验优化
- [ ] 性能优化
- [ ] 测试和调试

### 阶段五：部署准备 (1天)
- [ ] 构建配置优化
- [ ] 环境变量配置
- [ ] Docker部署配置
- [ ] 文档编写

## 6. 与后端服务对接

### API接口对接
1. 登录接口: `POST /api/v1/auth/login`
2. 注册接口: `POST /api/v1/auth/register`
3. 获取验证码: `GET /api/v1/auth/captcha`
4. 重置密码: `POST /api/v1/auth/reset-password`

### 认证流程
```
1. 用户在登录页面输入凭据
2. 前端调用认证服务API
3. 认证服务验证凭据并返回JWT Token
4. 前端存储Token (localStorage/cookie)
5. 跳转到主应用或指定页面
```

## 7. 部署架构建议

### 开发环境
- 使用Vite开发服务器
- 通过代理解决跨域问题

### 生产环境
- 使用Nginx托管静态文件
- 配置反向代理到API网关
- 启用Gzip压缩
- 配置HTTPS

### Nginx配置示例
```nginx
server {
    listen 80;
    server_name login.example.com;
    
    # SPA静态文件
    location / {
        root /usr/share/nginx/html;
        try_files $uri $uri/ /index.html;
        # 启用gzip压缩
        gzip on;
        gzip_types text/plain text/css application/json application/javascript text/xml application/xml;
    }
    
    # API请求转发到网关
    location /api/ {
        proxy_pass http://iam-gateway:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```

## 8. 安全考虑

1. XSS防护: 使用框架内置防护机制
2. CSRF防护: 实现Token验证
3. 输入验证: 前后端双重验证
4. 密码安全: 前端不处理密码加密，由后端处理
5. HTTPS: 生产环境强制使用HTTPS

## 9. 项目管理

### Git分支策略
- main: 生产环境代码
- develop: 开发环境代码
- feature/*: 功能开发分支
- hotfix/*: 紧急修复分支

### 提交规范
遵循Conventional Commits规范:
- feat: 新功能
- fix: 修复bug
- chore: 构建过程或辅助工具的变动
- docs: 文档更新
- style: 代码格式调整
- refactor: 重构代码
- perf: 性能优化
- test: 测试相关