import resultatsArborescence from "./resultatsArborescencePage.html";
import arborescence from "./rechercheArborescencePage.html";
import listeResultats from "./listeResultatsPage.html";
import métierDetail from "./métierDetailPage.html";
import recherche from "./recherchePage.html";
import { storiesOf } from "@storybook/html";

storiesOf("AEM Components / AEM Pages", resultatsArborescence).add(
  "Resultats par arborescence",
  () => resultatsArborescence
);
storiesOf("AEM Components / AEM Pages", arborescence).add(
  "Recherche arborescence",
  () => arborescence
);

storiesOf("AEM Components / AEM Pages", listeResultats).add(
  "Liste de resultats",
  () => listeResultats
);

storiesOf("AEM Components / AEM Pages", métierDetail).add(
  "Métier detail",
  () => métierDetail
);

storiesOf("AEM Components / AEM Pages", recherche).add(
  "Recherche métier",
  () => recherche
);
