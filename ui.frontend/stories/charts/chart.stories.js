import chart from "./chart.html";
import lineChartExample from "./lineChartExample.html";
import barChartExample from "./barChartExample.html";
import doughnutChartExample from "./doughnutChartExample.html";
import { storiesOf } from "@storybook/html";

storiesOf("AEM Components/Graphs", chart).add("Chart", () => chart);

storiesOf("AEM Components/Graphs", lineChartExample).add(
  "Line Chart Example",
  () => lineChartExample
);

storiesOf("AEM Components/Graphs", barChartExample).add(
  "Bar Chart Example",
  () => barChartExample
);

storiesOf("AEM Components/Graphs", doughnutChartExample).add(
  "Doughnut Chart Example",
  () => doughnutChartExample
);
