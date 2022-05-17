import forms from "./forms.html";
import formCitoyen from "./formCitoyen.html";
import formContactEntreprise from "./formContactEntreprise.html";
import demandeformation from "./demandeformation.html";
import stage from "./stage.html";
import demandeForm from "./demandeForm.html";
import reclamtion from "./reclamtion.html";
import { storiesOf } from "@storybook/html";

storiesOf("AEM Components/Formulaires", forms).add("Design", () => forms);
storiesOf("AEM Components/Formulaires", formCitoyen).add(
  "Citoyen",
  () => formCitoyen
);

storiesOf("AEM Components/Formulaires", formContactEntreprise).add(
  "Entreprise",
  () => formContactEntreprise
);

storiesOf("AEM Components/Formulaires", demandeformation).add(
  "Demande Formation",
  () => demandeformation
);

storiesOf("AEM Components/Formulaires", stage).add("Stage", () => stage);

storiesOf("AEM Components/Formulaires", demandeForm).add(
  "Demande",
  () => demandeForm
);

storiesOf("AEM Components/Formulaires", reclamtion).add(
  "Reclamtion",
  () => reclamtion
);
