// Git提交信息验证配置 - 基于Cursor规则10-git-workflow.mdc
module.exports = {
  extends: ['@commitlint/config-conventional'],
  rules: {
    // 类型枚举 - 与Cursor规则保持一致
    'type-enum': [2, 'always', ['feat', 'fix', 'docs', 'style', 'refactor', 'perf', 'test', 'chore', 'ci', 'build']],
    
    // 范围枚举 - 基于项目模块结构
    'scope-enum': [2, 'always', ['core', 'auth', 'system', 'audit', 'gateway', 'common', 'config', 'docs', 'deps']],
    
    // 基础验证规则
    'header-max-length': [2, 'always', 100],
    'type-case': [2, 'always', 'lower-case'],
    'type-empty': [2, 'never'],
    'scope-case': [2, 'always', 'lower-case'],
    'subject-case': [2, 'always', 'lower-case'],
    'subject-empty': [2, 'never'],
    'subject-full-stop': [2, 'never', '.'],
  },
};
