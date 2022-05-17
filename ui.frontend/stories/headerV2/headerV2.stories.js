import headerV2 from "./headerV2.html";
import headerV2Citoyen from "./headerV2Citoyen.html";
import headerV2Entreprise from "./headerV2Entreprise.html";
import headerV2Partenaire from "./headerV2Partenaire.html";
import { storiesOf } from "@storybook/html";

storiesOf("AEM Components / HeaderV2", headerV2).add(
  "HeaderV2",
  () => headerV2
);
storiesOf("AEM Components / HeaderV2", headerV2).add(
  "HeaderV2 Citoyen",
  () => headerV2Citoyen
);
storiesOf("AEM Components / HeaderV2", headerV2).add(
  "HeaderV2 Entreprise",
  () => headerV2Entreprise
);
storiesOf("AEM Components / HeaderV2", headerV2).add(
  "HeaderV2 Partenaire",
  () => headerV2Partenaire
);
