import rechercheMetier from "./rechercheMetier.html";
import rechercheMetierShort from "./rechercheMetierShort.html";
import { storiesOf } from "@storybook/html";

storiesOf("AEM Components/Recherche Metier", rechercheMetier).add(
  "Recherche Metier",
  () => rechercheMetier
);

storiesOf("AEM Components/Recherche Metier", rechercheMetierShort).add(
  "Recherche Metier Short",
  () => rechercheMetierShort
);
