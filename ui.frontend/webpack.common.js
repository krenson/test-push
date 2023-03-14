"use strict";

const path = require("path");
const webpack = require("webpack");
const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const CopyWebpackPlugin = require("copy-webpack-plugin");
const { CleanWebpackPlugin } = require("clean-webpack-plugin");
const AemClientlibGeneratorPlugin = require("aem-clientlib-generator-webpack-plugin");

const SOURCE_ROOT = __dirname + "/src/main/webpack";
const DIST_ROOT_STATIC_JS =
  "../../ui.content/src/main/content/jcr_root/content/dam/leforemhe/fr/javascript/static";
const DIST_WC_FONTS =
  __dirname + "/node_modules/@le-forem-dsi/forem-common-style";

module.exports = (env) => {
  return {
    resolve: {
      extensions: [".js"],
      alias: {
        webComponent: path.join(__dirname, "node_modules/@le-forem-dsi"),
      },
    },
    entry: {
      site: SOURCE_ROOT + "/site/site.js",
      dependencies: SOURCE_ROOT + "/site/leforemhe.js",
      phpapplication: SOURCE_ROOT + "/site/leforemhe-php-application.js",
      layout: SOURCE_ROOT + "/site/leforemhe-layout.js"},
    output: {
      filename: (chunkData) => {
        switch (chunkData.chunk.name) {
          case "dependencies":
            return "clientlib-dependencies/[name].js";
          case "phpapplication":
            return "clientlib-phpapplication/[name].js";
          case "layout":
            return "clientlib-layout/[name].js";
          default:
            return "clientlib-site/[name].js";
        }
      },
      path: path.resolve(__dirname, "dist"),
    },
    module: {
      rules: [
        {
          test: /\.html$/i,
          loader: "html-loader",
        },
        {
          test: /(\.js$)/,
          exclude: [/(node_modules)/],
          use: [
            {
              loader: "babel-loader",
              options: {
                presets: ["@babel/preset-env"],
                plugins: ["transform-class-properties"],
              },
            },
            {
              loader: "webpack-import-glob-loader",
              options: {
                url: false,
              },
            },
          ],
        },
        {
          test: /\.(css|scss)$/,
          use: [
            MiniCssExtractPlugin.loader,
            {
              loader: "css-loader",
              options: {
                url: false,
              },
            },
            {
              loader: "postcss-loader",
              options: {
                plugins() {
                  return [require("autoprefixer")];
                },
              },
            },
            {
              loader: "sass-loader",
              options: {
                url: false,
                data:
                  "$resourcePath: " +
                  env.resourcePath +
                  ";" +
                  "$resourceFonts: " +
                  env.resourceFonts +
                  ";",
              },
            },
            {
              loader: "webpack-import-glob-loader",
              options: {
                url: false,
              },
            },
          ],
        },
      ],
    },
    plugins: [
      new CleanWebpackPlugin(),
      new webpack.NoEmitOnErrorsPlugin(),
      new MiniCssExtractPlugin({
        filename: "clientlib-[name]/[name].css",
      }),
      new CopyWebpackPlugin([
        {
          from: path.resolve(__dirname, SOURCE_ROOT + "/dependencies/site/js"),
          to: DIST_ROOT_STATIC_JS,
        },
        {
          from: path.resolve(__dirname, SOURCE_ROOT + "/resources"),
          to: "./clientlib-site/resources",
        }
      ]),
      new AemClientlibGeneratorPlugin(),
    ],
    stats: {
      assetsSort: "chunks",
      builtAt: true,
      children: false,
      chunkGroups: true,
      chunkOrigins: true,
      colors: false,
      errors: true,
      errorDetails: true,
      env: true,
      modules: false,
      performance: true,
      providedExports: false,
      source: false,
      warnings: true,
    },
  };
};
