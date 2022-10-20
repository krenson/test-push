import teaser from "./teaser.html";
import navigation from "./navigation.html";
import { storiesOf } from "@storybook/html";

storiesOf("AEM Components/LinkIcone", navigation).add(
  "Navigation",
  () => navigation
);
storiesOf("AEM Components/LinkIcone", teaser).add("Teaser", () => teaser);
