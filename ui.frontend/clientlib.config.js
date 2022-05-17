module.exports = {
  // default working directory (can be changed per 'cwd' in every asset option)
  context: __dirname,

  // path to the clientlib root folder (output)
  clientLibRoot:
    "./../ui.apps/src/main/content/jcr_root/apps/leforemhe/clientlibs",

  libs: [
    {
      name: "clientlib-dependencies",
      allowProxy: true,
      categories: ["leforemhe.dependencies"],
      serializationFormat: "xml",
      cssProcessor: ["default:none", "min:none"],
      jsProcessor: ["default:none", "min:none"],
      assets: {
        css: ["dist/clientlib-dependencies/*.css"],
      },
    },
    {
      name: "clientlib-site",
      allowProxy: true,
      categories: ["leforemhe.site"],
      dependencies: ["leforemhe.dependencies"],
      serializationFormat: "xml",
      cssProcessor: ["default:none", "min:none"],
      jsProcessor: ["default:none", "min:none"],
      assets: {
        css: ["dist/clientlib-site/*.css"],
        resources: [
          { src: "dist/clientlib-site/resources/images/*.*", dest: "images/" },
          {
            src: "dist/clientlib-site/resources/images/socialmedia/*.*",
            dest: "images/socialmedia/",
          },
          {
            src: "dist/clientlib-site/resources/images/boutons/*.*",
            dest: "images/boutons/",
          },
          {
            src: "dist/clientlib-site/resources/images/fancybox/*.*",
            dest: "images/fancybox/",
          },
          {
            src: "dist/clientlib-site/resources/images/contacts/*.*",
            dest: "images/contacts/",
          },
          {
            src: "dist/clientlib-site/resources/images/doc/*.*",
            dest: "images/doc/",
          },
          {
            src: "dist/clientlib-site/resources/images/logos/*.*",
            dest: "images/logos/",
          },
          {
            src: "dist/clientlib-site/resources/images/puces/*.*",
            dest: "images/puces/",
          },
          {
            src: "dist/clientlib-site/resources/images/fonds/*.*",
            dest: "images/fonds/",
          },
          {
            src: "dist/clientlib-site/resources/images/footer/*.*",
            dest: "images/footer/",
          },
          {
            src: "dist/clientlib-site/resources/images/header/*.*",
            dest: "images/header/",
          },
          {
            src: "dist/clientlib-site/resources/images/multi/*.*",
            dest: "images/multi/",
          },
          {
            src: "dist/clientlib-site/resources/images/icons/*.*",
            dest: "images/icons/",
          },
          {
            src: "dist/clientlib-site/resources/images/forms/*.*",
            dest: "images/forms/",
          },
          /*  { src: "dist/clientlib-foremWebComponent/resources/fonts/*.*", dest: "fonts/" }, */
          { src: "dist/clientlib-site/resources/lang/*.*", dest: "lang/" },
        ],
      },
    },
    {
      name: "clientlib-phpapplication",
      allowProxy: true,
      categories: ["leforemhe.php.application"],
      serializationFormat: "xml",
      cssProcessor: ["default:none", "min:none"],
      jsProcessor: ["default:none", "min:none"],
      assets: {
        css: ["dist/clientlib-phpapplication/*.css"],
      },
    },
    {
      name: "clientlib-layout",
      allowProxy: true,
      categories: ["leforemhe.layout"],
      serializationFormat: "xml",
      cssProcessor: ["default:none", "min:none"],
      jsProcessor: ["default:none", "min:none"],
      assets: {
        css: ["dist/clientlib-layout/*.css"],
      },
    },
    {
      name: "clientlib-formapass",
      allowProxy: true,
      categories: ["leforemhe.formapass"],
      serializationFormat: "xml",
      cssProcessor: ["default:none", "min:none"],
      jsProcessor: ["default:none", "min:none"],
      assets: {
        css: ["dist/clientlib-formapass/*.css"],
        css: ["dist/clientlib-formapass/*.js"],
      },
    },
    {
      name: "clientlib-foremWebComponent",
      allowProxy: true,
      categories: ["leforemhe.foremWebComponent", "leforemhe.site"],
      serializationFormat: "xml",
      cssProcessor: ["default:none", "min:none"],
      jsProcessor: ["default:none", "min:none"],
      assets: {
        css: ["dist/clientlib-foremWebComponent/*.css"],
        js: ["dist/clientlib-foremWebComponent/*.js"],
        resources: [
          {
            src: "dist/clientlib-foremWebComponent/resources/fonts/*.*",
            dest: "fonts/",
          },
          {
            src: "dist/clientlib-foremWebComponent/resources/fonts/fontawesome/*.*",
            dest: "fonts/fontawesome/",
          },
        ],
      },
    },
  ],
};
