import container from "./container.html";
import page2 from "./page2.html";
import page3 from "./page3.html";
import page4 from "./page4.html";
import page5 from "./page5.html";
import { storiesOf } from "@storybook/html";

storiesOf("AEM Components/Container", container).add(
  "Container Example",
  () => container
);
storiesOf("AEM Components/Container", page2).add("Page 2", () => page2);
storiesOf("AEM Components/Container", page3).add("Page 3", () => page3);
storiesOf("AEM Components/Container", page4).add("Page 4", () => page4);
storiesOf("AEM Components/Container", page5).add("Page 5", () => page5);
