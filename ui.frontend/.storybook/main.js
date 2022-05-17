// your app's webpack.config.js
const custom = require("../webpack.common");

module.exports = (env) => ({
  stories: ["../stories/**/*.stories.*"],
  addons: [
    "@storybook/addon-a11y",
    "@whitespace/storybook-addon-html",
    "@storybook/addon-viewport",
  ],
  webpackFinal: (config) => {
    return {
      ...config,
      module: { ...config.module, rules: custom(env).module.rules },
    };
  },
});
