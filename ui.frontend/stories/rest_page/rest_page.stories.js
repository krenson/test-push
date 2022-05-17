import rest_page from "./rest_page.html";
import rest_page2 from "./rest_page2.html";
import rest_page3 from "./rest_page3.html";
import rest_page4 from "./rest_page4.html";
import { storiesOf } from "@storybook/html";

storiesOf("AEM Components/Rest Page", rest_page).add(
  "Rest Page 1",
  () => rest_page
);

storiesOf("AEM Components/Rest Page", rest_page2).add(
  "Rest Page 2",
  () => rest_page2
);

storiesOf("AEM Components/Rest Page", rest_page3).add(
  "Rest Page 3",
  () => rest_page3
);

storiesOf("AEM Components/Rest Page", rest_page4).add(
  "Rest Page 4",
  () => rest_page4
);
