import formapass from "./formapass.html";
import resultatrecherchesimple from "./resultatrecherchesimple.html";
import rechercherParMotCle from "./rechercherParMotCle.html";
import rechercheparcreiteres from "./rechercheparcreiteres.html";
import diffusersonoffre from "./diffusersonoffre.html";
import demandederenseignement from "./demandederenseignement.html";
import contactcarrefouremploi from "./contactcarrefouremploi.html";

import detailoffre from "./detailoffre.html";
import { storiesOf } from "@storybook/html";

storiesOf("AEM Components/Formapass", formapass).add(
  "Page acceuil",
  () => formapass
);
storiesOf("AEM Components/Formapass", resultatrecherchesimple).add(
  "Resultat recherche simple",
  () => resultatrecherchesimple
);
storiesOf("AEM Components/Formapass", detailoffre).add(
  "Details offre",
  () => detailoffre
);
storiesOf("AEM Components/Formapass", rechercherParMotCle).add(
  "Rechercher par mot cle",
  () => rechercherParMotCle
);
storiesOf("AEM Components/Formapass", rechercheparcreiteres).add(
  "Rechercher par critères",
  () => rechercheparcreiteres
);
storiesOf("AEM Components/Formapass", diffusersonoffre).add(
  "Diffuser son offre",
  () => diffusersonoffre
);
storiesOf("AEM Components/Formapass", demandederenseignement).add(
  "Demande de renseignement",
  () => demandederenseignement
);

storiesOf("AEM Components/Formapass", contactcarrefouremploi).add(
  "Contacter un Carrefour Emploi Formation Orientation",
  () => contactcarrefouremploi
);
