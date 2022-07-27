import popup from "./popup.html";
import popupbase from "./popupbase.html";

import { storiesOf } from "@storybook/html";
storiesOf("AEM Components/Popup", popupbase).add("Popup base", () => popupbase);
storiesOf("AEM Components/Popup", popup).add("Popup in Condition", () => popup);
