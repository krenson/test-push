import imagesCurrent from "./images-current.html";
import imagesNew from "./images-new.html";
import { storiesOf } from "@storybook/html";

storiesOf("AEM Components/images", imagesCurrent).add(
  "Images Current",
  () => imagesCurrent
);

storiesOf("AEM Components/images", imagesNew).add(
  "Images New",
  () => imagesNew
);
