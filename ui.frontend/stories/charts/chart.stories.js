import chart from "./chart.html";
import lineChartExample from "./lineChartExample.html";
import barChartExample from "./barChartExample.html";
import { storiesOf } from "@storybook/html";

storiesOf("AEM Components/chart", chart).add("Chart", () => chart);

storiesOf("AEM Components/chart", lineChartExample).add(
  "Line Chart Example",
  () => lineChartExample
);

storiesOf("AEM Components/chart", barChartExample).add(
  "Bar Chart Example",
  () => barChartExample
);
