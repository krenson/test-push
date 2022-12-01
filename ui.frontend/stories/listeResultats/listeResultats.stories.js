import listeResultatsMore from "./listeResultatsMore.html";
import listeResultatsOne from "./listeResultatsOne.html";
import listeResultatsZero from "./listeResultatsZero.html";
import { storiesOf } from "@storybook/html";

storiesOf("AEM Components/Liste Resultats", listeResultatsMore).add(
  "Liste Resultats (+99)",
  () => listeResultatsMore
);

storiesOf("AEM Components/Liste Resultats", listeResultatsOne).add(
  "Liste Resultats (1)",
  () => listeResultatsOne
);

storiesOf("AEM Components/Liste Resultats", listeResultatsZero).add(
  "Liste Resultats (0)",
  () => listeResultatsZero
);
