import rechercheMetier from "./rechercheMetier.html";
import rechercheMetierForm from "./rechercheMetierForm.html";
import { storiesOf } from "@storybook/html";

storiesOf("AEM Components/Recherche Metier", rechercheMetier).add(
  "Recherche Metier",
  () => rechercheMetier
);

storiesOf("AEM Components/Recherche Metier", rechercheMetierForm).add(
  "Recherche Metier Form",
  () => rechercheMetierForm
);
