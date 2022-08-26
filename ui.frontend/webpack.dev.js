const merge = require("webpack-merge");
const common = require("./webpack.common.js");
const path = require("path");
const HtmlWebpackPlugin = require("html-webpack-plugin");
const CopyWebpackPlugin = require("copy-webpack-plugin");

const SOURCE_ROOT = __dirname + "/src/main/webpack";
const SOURCE_ROOT_DYNAMIC_JS = __dirname + "/.storybook/dynamic-js";

module.exports = (env) =>
  merge(common(env), {
    mode: "development",
    devtool: "inline-source-map",
    performance: { hints: "warning" },
    plugins: [
      new CopyWebpackPlugin([
        {
          from: path.resolve(__dirname, SOURCE_ROOT + "/dependencies/site/js"),
          to: "./clientlib-site/resources/js",
        },
        {
          from: path.resolve(__dirname, SOURCE_ROOT_DYNAMIC_JS),
          to: "./clientlib-site/resources/js",
        },
      ]),
      new HtmlWebpackPlugin({
        template: path.resolve(__dirname, SOURCE_ROOT + "/static/index.html"),
      }),
      /*  new HtmlWebpackPlugin({
            filename: "test.html",
            template: path.resolve(__dirname, SOURCE_ROOT + '/static/test.html')
        }),

        new HtmlWebpackPlugin({
                    filename: "pge_resp_corps.html",
                    template: path.resolve(__dirname, SOURCE_ROOT + '/static/pge_resp_corps.html')
                }),

        new HtmlWebpackPlugin({
                    filename: "pge_resp_homepage.html",
                    template: path.resolve(__dirname, SOURCE_ROOT + '/static/pge_resp_homepage.html')
                }) */
    ],
    devServer: {
      headers: {
        "Access-Control-Allow-Origin": "*",
        "Access-Control-Allow-Methods":
          "GET, POST, PUT, DELETE, PATCH, OPTIONS",
        "Access-Control-Allow-Headers":
          "X-Requested-With, content-type, Authorization",
      },
      inline: true,
      port: 8081,
      disableHostCheck: true,
      proxy: [
        {
          context: ["/content", "/etc.clientlibs"],
          target: "http://localhost:4502",
        },
      ],
    },
  });
