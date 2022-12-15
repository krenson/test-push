import navigation from "./navigation.html";
import teaserPopup from "./teaserPopup.html";
import teaserLink from "./teaserLink.html";

import { storiesOf } from "@storybook/html";

storiesOf("AEM Components/Link Icone", navigation).add(
  "Navigation",
  () => navigation
);

storiesOf("AEM Components/Link Icone", teaserPopup).add(
  "Teaser Popup",
  () => teaserPopup
);

storiesOf("AEM Components/Link Icone", teaserLink).add(
  "Teaser Link",
  () => teaserLink
);
