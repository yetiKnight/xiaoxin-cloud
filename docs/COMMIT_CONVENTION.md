# Git Commit 规范

本项目采用 [Conventional Commits](https://www.conventionalcommits.org/) 规范来标准化提交信息。

## 提交格式

```
<type>(<scope>): <subject>

<body>

<footer>
```

## Type 类型

| 类型 | 说明 | 示例 |
|------|------|------|
| `feat` | 新功能 | `feat(auth): add OAuth2 login support` |
| `fix` | 修复bug | `fix(core): resolve user creation validation issue` |
| `docs` | 文档更新 | `docs: update API documentation` |
| `style` | 代码格式调整（不影响功能） | `style: format code according to style guide` |
| `refactor` | 重构代码 | `refactor(core): simplify user service logic` |
| `perf` | 性能优化 | `perf(auth): optimize token validation performance` |
| `test` | 测试相关 | `test(core): add unit tests for user service` |
| `chore` | 构建过程或辅助工具的变动 | `chore: update dependencies` |
| `ci` | 持续集成相关 | `ci: add GitHub Actions workflow` |
| `build` | 构建系统或外部依赖的变动 | `build: update Maven configuration` |

## Scope 范围

| 范围 | 说明 |
|------|------|
| `core` | 核心服务 |
| `auth` | 认证服务 |
| `system` | 系统服务 |
| `audit` | 审计服务 |
| `config` | 配置相关 |
| `docs` | 文档 |
| `deps` | 依赖管理 |

## Subject 主题

- 使用现在时态，如"add"而不是"added"或"adds"
- 首字母小写
- 结尾不加句号
- 不超过50个字符

## Body 正文

- 解释为什么做这个改动
- 与之前版本有什么不同
- 每行不超过72个字符

## Footer 脚注

- 关闭的Issue: `Closes #123`
- 破坏性变更: `BREAKING CHANGE: 详细说明`

## 示例

### 新功能
```
feat(auth): add JWT token refresh mechanism

Implement automatic token refresh when the access token is about to expire.
This improves user experience by reducing login interruptions.

Closes #45
```

### 修复bug
```
fix(core): resolve user creation validation issue

The user creation was failing when email validation was enabled.
Fixed the validation logic to properly handle edge cases.

Fixes #23
```

### 重构
```
refactor(system): simplify permission checking logic

Extract permission checking into a separate service to improve
code reusability and maintainability.

BREAKING CHANGE: PermissionService interface has changed
```

### 文档更新
```
docs: update API documentation for user endpoints

Add detailed examples and parameter descriptions for all user
management endpoints.
```

## 工具配置

项目已配置以下工具来帮助遵循规范：

1. **Git Commit Template**: 使用 `git commit` 时会自动加载模板
2. **Commitlint**: 验证提交信息格式（需要安装相关依赖）

## 使用说明

1. 使用 `git commit` 时会自动打开模板
2. 按照模板格式填写提交信息
3. 确保提交信息符合规范要求

## 注意事项

- 每次提交应该只包含一个逻辑变更
- 提交信息应该清晰描述变更内容
- 对于破坏性变更，必须在footer中说明
- 关联的Issue应该在footer中引用
