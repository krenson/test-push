import teaserList1Col from "./teaserList1Col.html";
import teaserList2Col from "./teaserList2Col.html";
import teaserList3Col from "./teaserList3Col.html";
import teaserListRow from "./teaserListRow.html";
import { storiesOf } from "@storybook/html";

storiesOf("AEM Components/Teaser List", teaserList1Col).add(
  "Teaser List 1 Column",
  () => teaserList1Col
);

storiesOf("AEM Components/Teaser List", teaserList2Col).add(
  "Teaser List 2 Columns",
  () => teaserList2Col
);

storiesOf("AEM Components/Teaser List", teaserList3Col).add(
  "Teaser List 3 Columns",
  () => teaserList3Col
);

storiesOf("AEM Components/Teaser List", teaserListRow).add(
  "Teaser List Row",
  () => teaserListRow
);
