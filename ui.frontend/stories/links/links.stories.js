import links from "./links.html";
import links2 from "./links2.html";
import { storiesOf } from "@storybook/html";

storiesOf("AEM Components/Links", links).add("Links", () => links);

storiesOf("AEM Components/Links", links2).add("Links Onglet", () => links2);
