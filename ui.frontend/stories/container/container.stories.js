import container from "./container.html";
import page2 from "./page2.html";
import { storiesOf } from "@storybook/html";

storiesOf("AEM Components/Container", container).add(
  "Container Example",
  () => container
);
storiesOf("AEM Components/Container", page2).add("Page 2", () => page2);
