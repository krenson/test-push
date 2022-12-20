import containerStyling from "./containerStyling.html";
import { storiesOf } from "@storybook/html";

storiesOf("AEM Components", containerStyling).add(
  "Container Styling",
  () => containerStyling
);
