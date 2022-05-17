import carrousselHomepage from "./carrousselHomepage.html";
import carrousselHomepage2 from "./carrousselHomepage2.html";
import { storiesOf } from "@storybook/html";

storiesOf("AEM Components/Carroussel Homepage", carrousselHomepage).add(
  "Carroussel 3 Slides",
  () => carrousselHomepage
);

storiesOf("AEM Components/Carroussel Homepage", carrousselHomepage2).add(
  "Carroussel 1 Slide",
  () => carrousselHomepage2
);
