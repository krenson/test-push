import teaser from "./teaser.html";
import navigation from "./navigation.html";
import { storiesOf } from "@storybook/html";

storiesOf("AEM Components/Link Icone", navigation).add(
  "Navigation",
  () => navigation
);
storiesOf("AEM Components/Link Icone", teaser).add("Teaser", () => teaser);
