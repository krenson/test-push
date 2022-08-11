import filtrebase from "./filtrebase.html";
import filtre from "./filtre.html";
import { storiesOf } from "@storybook/html";

storiesOf("AEM Components/Filtre", filtrebase).add(
  "Filtre base",
  () => filtrebase
);
storiesOf("AEM Components/Filtre", filtre).add(
  "Filtre in condition",
  () => filtre
);
