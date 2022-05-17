import rte from "./rte.html";
import rte2 from "./rte2.html";
import rte3 from "./rte3.html";
import rte4 from "./rte4.html";
import { storiesOf } from "@storybook/html";

storiesOf("AEM Components/Rich Text Editor", rte).add("Rte1", () => rte);
storiesOf("AEM Components/Rich Text Editor", rte2).add("Rte2", () => rte2);
storiesOf("AEM Components/Rich Text Editor", rte3).add("Rte3", () => rte3);
storiesOf("AEM Components/Rich Text Editor", rte4).add("Rte4", () => rte4);
