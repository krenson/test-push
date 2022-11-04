import verredevin from "./verredevin.html";
import verredevin2 from "./verredevin2.html";
import { storiesOf } from "@storybook/html";

storiesOf("AEM Components/Verre De Vin", verredevin).add(
  "base",
  () => verredevin
);

storiesOf("AEM Components/Verre De Vin", verredevin2).add(
  "nouveau",
  () => verredevin2
);
