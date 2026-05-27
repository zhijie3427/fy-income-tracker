module.exports = {
  root: true,
  env: {
    browser: true,
    node: true,
    es2021: true,
  },
  parser: 'vue-eslint-parser',
  parserOptions: {
    ecmaVersion: 2021,
    sourceType: 'module',
    ecmaFeatures: {
      jsx: false,
    },
  },
  extends: ['plugin:vue/vue3-recommended', 'eslint:recommended'],
  plugins: ['vue'],
  rules: {
    'vue/script-setup-uses-vars': 'error',
    'vue/no-multiple-template-root': 'off',
    'vue/multi-word-component-names': 'off',
  },
}
