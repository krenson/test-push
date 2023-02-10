import Chart from "chart.js/dist/chart";

const colors = [
  "rgb(47, 58, 72)",
  "rgb(251, 231, 203)",
  "rgb(255, 142, 151)",
  "rgb(255, 255, 255)",
  "rgb(222, 199, 224)",
  "rgb(150, 156, 163)",
  "rgb(252, 219, 173)",
];

const colorBorders = [
  "rgb(47, 58, 72)",
  "rgb(251, 231, 203)",
  "rgb(255, 142, 151)",
  "rgb(54, 64, 75)",
  "rgb(222, 199, 224)",
  "rgb(150, 156, 163)",
  "rgb(54, 64, 75)",
];

const SERVLET_URL = "/content/leforemhe/graphdataservlet.json";
const ATTRIBUTE_GRAPH_DATA_PATH = "data-chartdata-path";
const ATTRIBUTE_SHOW_AS_PERCENTAGE = "data-chartdata-showaspercentage";
const ATTRIBUTE_CHART_TYPE = "data-chartdata-charttype";

const CHART = {
  HorizontalBar: "horizontal",
  VerticalBar: "vertical",
};

const getChartType = (chartType) => {
  switch (chartType) {
    case "horizontal":
      return CHART.HorizontalBar;
    case "vertical":
      return CHART.VerticalBar;
    default:
      return CHART.HorizontalBar;
  }
};

const getChartTypeOption = (chartType) => {
  switch (chartType) {
    case chartType === CHART.HorizontalBar:
      return "bar";
    case chartType === CHART.VerticalBar:
      return "bar";
    default:
      return "bar";
  }
};

const renderGraph = (showAsPercentage, barChartElement, data, chartType) => {
  const resolvedChartType = getChartType(chartType);
  const chartConfig = {
    type: getChartTypeOption(resolvedChartType),
    data: {
      labels: data.labels,
      datasets: getGeneratedDataset(data.datasets),
    },
    options: {
      scaleShowValues: true,
      plugins: {
        tooltip: {
          titleAlign: "center",
          padding: 20,
          displayColors: false,
          backgroundColor: "#ffffff",
          footerColor: "blue",
          titleColor: "#2f3a48",
          bodyColor: "#2f3a48",
          borderColor: "#fbe7cb",
          borderWidth: 1,
          callbacks: {
            title: function (tooltipItem) {
              let tooltipTitle = tooltipItem[0].label.split("&").join(" ");
              return tooltipTitle;
            },

            label: function (tooltipItem) {
              let chartDatasets = [];
              let selectedData;

              let tooltipArrString = [];

              // get all the relevant data: label + data array

              for (i = 0; i < chartConfig.data.labels.length; i++) {
                let legend = chartConfig.data.labels[i];
                let dataset = [];
                var j = 0;
                for (j; j < chartConfig.data.datasets.length; j++) {
                  dataset.push({
                    label: chartConfig.data.datasets[j].label,
                    data: chartConfig.data.datasets[j].data[i],
                  });
                }

                chartDatasets.push({ legend: legend, data: dataset });
              }

              // selected data on the chart
              selectedData = {
                legend: tooltipItem.label,
                label: tooltipItem.dataset.label,
                data: tooltipItem.raw,
              };

              // look for the right data based on wat the tooltip selected
              // return every data set of the chart legend

              for (i = 0; i < chartDatasets.length; i++) {
                if (chartDatasets[i].legend == selectedData.legend) {
                  chartDatasets[i].data.forEach((set) => {
                    tooltipArrString.push(
                      `${set.data} % pour ${set.label}`.toLocaleLowerCase()
                    );

                    if (chartDatasets[i].data.length == 2) {
                      let string = tooltipArrString[0].split("pour");
                      let newString = string[0] + "pour ce métier";

                      tooltipArrString[0] = newString;
                    }
                  });
                }
              }

              return tooltipArrString;
            },
          },
        },
        legend: {
          display: data.datasets.length > 1 ? true : false,
          align: "center",
          labels: {
            font: {
              family: "Poppins",
              size: 15,
            },
          },
        },
      },
      layout: {
        padding: {
          left: 10,
        },
      },
      indexAxis: resolvedChartType == CHART.HorizontalBar ? "y" : "x",
      scales: {
        y: {
          grid: {
            display: resolvedChartType == CHART.VerticalBar,
            borderColor: "rgb(219, 202, 175)",
            borderWidth: 2,
          },
          ticks: {
            autoSkip: false,
            font: {
              family: "Poppins",
              size: 13,
            },
            callback: function (value) {
              if (resolvedChartType == CHART.HorizontalBar) {
                const valueLegend = this.getLabelForValue(value);
                const arr = valueLegend.split("&");
                return arr;
              } else if (resolvedChartType == CHART.VerticalBar) {
                return `${value}${showAsPercentage ? "%" : ""}`;
              }
            },
          },
        },
        x: {
          labelMaxWidth: 20,
          beginAtZero: true,
          grid: {
            display: resolvedChartType == CHART.HorizontalBar,
            color: "rgb(246, 241, 234)",
            lineWidth: 2,
            borderColor: "rgb(219, 202, 175)",
            borderWidth: 2.5,
            radius: "50%",
          },
          ticks: {
            autoSkip: false,
            font: {
              size: 15,
            },
            callback: function (value) {
              if (resolvedChartType == CHART.VerticalBar) {
                const valueLegend = this.getLabelForValue(value);
                const arr = valueLegend.split("&");
                return arr;
              } else if (resolvedChartType == CHART.HorizontalBar) {
                return `${value}${showAsPercentage ? "%" : ""}`;
              }
            },
          },
        },
      },
    },
    plugins: [
      {
        id: "legendMargin",
        beforeInit(chart, legend, options) {
          const fitValue = chart.legend.fit;
          chart.legend.fit = function fit() {
            fitValue.bind(chart.legend)();
            return (this.height += 30);
          };
        },
      },
    ],
  };
  const barChart = new Chart(barChartElement, chartConfig);
};

const hasAllData = (dataset) => {
  return dataset && dataset.labels && dataset.datasets;
};

const getGeneratedDataset = (datasetResponse) => {
  const dataset = [];
  datasetResponse.forEach((data, index) => {
    dataset.push({
      label: data.label,
      data: data.data,
      fill: false,
      backgroundColor: colors[index],
      borderColor: colorBorders[index],
    });
  });
  return dataset;
};

addEventListener("DOMContentLoaded", () => {
  let headers;
  if (location.hostname == "localhost") {
    headers = new Headers({
      Authorization: `Basic ${btoa("admin:admin")}`,
    });
  }

  const barChartsElements = document.querySelectorAll(
    'div[class="cmp-chart"] > canvas[class="cmp-chart--barchart"]'
  );

  barChartsElements.forEach((barChartElement) => {
    fetch(
      `${SERVLET_URL}?chartDataPath=${barChartElement.getAttribute(
        ATTRIBUTE_GRAPH_DATA_PATH
      )}`,
      { headers: headers, credentials: "include" }
    )
      .then((response) => response.json())
      .then((jsonData) => {
        if (hasAllData(jsonData)) {
          renderGraph(
            barChartElement.hasAttribute(ATTRIBUTE_SHOW_AS_PERCENTAGE)
              ? true
              : false,
            barChartElement,
            jsonData,
            barChartElement.getAttribute(ATTRIBUTE_CHART_TYPE)
          );
        } else {
          barChartElement.remove();
        }
      })
      .catch((error) => {
        console.log(error);
      });
  });
});
