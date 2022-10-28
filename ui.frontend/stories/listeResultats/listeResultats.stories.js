import listeResultatsMore from "./listeResultatsMore.html";
import listeResultatsOne from "./listeResultatsOne.html";
import listeResultatsZero from "./listeResultatsZero.html";
import { storiesOf } from "@storybook/html";

storiesOf("AEM Components/Liste Resultats", listeResultatsMore).add(
  "Liste Resultats More",
  () => listeResultatsMore
);

storiesOf("AEM Components/Liste Resultats", listeResultatsOne).add(
  "Liste Resultats One",
  () => listeResultatsOne
);

storiesOf("AEM Components/Liste Resultats", listeResultatsZero).add(
  "Liste Resultats Zero",
  () => listeResultatsZero
);
