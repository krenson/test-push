import filArianneTitreAccrocheMessage from "./filArianneTitreAccrocheMessage.html";
import filArianne from "./filArrianne.html";
import TitreAccroche from "./TitreAccroche.html";
import { storiesOf } from "@storybook/html";

storiesOf("AEM Components / Article Head", filArianneTitreAccrocheMessage).add(
  "Article Head FULL",
  () => filArianneTitreAccrocheMessage
);

storiesOf("AEM Components / Article Head", filArianne).add(
  "File Arianne",
  () => filArianne
);

storiesOf("AEM Components / Article Head", TitreAccroche).add(
  "Titre Accroche Message",
  () => TitreAccroche
);
