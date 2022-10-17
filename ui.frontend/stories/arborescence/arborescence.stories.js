import arborescence from "./arborescence.html";
import { storiesOf } from "@storybook/html";

storiesOf("AEM Components", arborescence).add(
  "Recherche par arborescence",
  () => arborescence
);
