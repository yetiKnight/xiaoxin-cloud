# IAM登录前端应用

## 简介
IAM登录前端应用是基于Vue3的单页应用，用于处理用户认证相关功能，包括登录、注册、密码重置等。

## 技术栈
- Vue 3 (Composition API)
- Vue Router 4
- Pinia (状态管理)
- Vite (构建工具)
- Element Plus (UI组件库)
- Axios (HTTP客户端)

## 开发环境搭建

### 环境要求
- Node.js >= 16.0.0
- npm >= 8.0.0

### 安装依赖
```bash
npm install
```

### 启动开发服务器
```bash
npm run dev
```

### 构建生产版本
```bash
npm run build
```

### 代码检查
```bash
npm run lint
```

## 项目结构
```
iam-login-frontend/
├── public/
│   ├── favicon.ico
│   └── index.html
├── src/
│   ├── assets/
│   ├── components/
│   ├── views/
│   ├── router/
│   ├── store/
│   ├── utils/
│   ├── styles/
│   ├── App.vue
│   └── main.js
├── package.json
└── vite.config.js
```

## 配置说明
- 开发环境配置位于 `vite.config.js`
- 路由配置位于 `src/router/index.js`
- 状态管理位于 `src/store/index.js`

## 部署
构建后的文件位于 `dist` 目录，可部署到Nginx等静态服务器中。