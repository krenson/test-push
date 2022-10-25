import rechercheMetier from "./rechercheMetier.html";
import rechercheMetier2 from "./rechercheMetier2.html";
import rechercheMetierForm from "./rechercheMetierForm.html";
import { storiesOf } from "@storybook/html";

storiesOf("AEM Components/Recherche Metier", rechercheMetier).add(
  "Recherche Metier",
  () => rechercheMetier
);

storiesOf("AEM Components/Recherche Metier", rechercheMetier2).add(
  "Recherche Metier 2",
  () => rechercheMetier2
);

storiesOf("AEM Components/Recherche Metier", rechercheMetierForm).add(
  "Recherche Metier Form",
  () => rechercheMetierForm
);
