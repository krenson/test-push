var colors = [
  "rgb(47, 58, 72)",
  "rgb(251, 231, 203)",
  "rgb(255, 142, 151)",
  "rgb(255, 255, 255)",
  "rgb(222, 199, 224)",
  "rgb(150, 156, 163)",
  "rgb(252, 219, 173)",
];

var colorBorders = [
  "rgb(47, 58, 72)",
  "rgb(251, 231, 203)",
  "rgb(255, 142, 151)",
  "rgb(54, 64, 75)",
  "rgb(222, 199, 224)",
  "rgb(150, 156, 163)",
  "rgb(54, 64, 75)",
];

// DOUGHNUT CHART EXAMPLE

// chart 1
var chartLabel = "Agent d'entretien&des parcs et jardins";

// datasets
var data = {
  labels: ["Services publics", "CDD", "CDI", "Interim"],
  datasets: [
    {
      chartLabel,
      data: [0, 14, 10, 76],
      backgroundColor: [colors[0], colors[1], colors[2], colors[3]],
      borderColor: [
        colorBorders[0],
        colorBorders[1],
        colorBorders[2],
        colorBorders[3],
      ],
      hoverOffset: 0,
      cutout: "80%",
    },
  ],
};

// plugins
var insideLabel = {
  id: "insideLabel",
  beforeDraw(chart, args, options) {
    const {
      ctx,
      chartArea: { top, right, bottom, left, width, height },
    } = chart;

    ctx.save();

    ctx.fillStyle = options.fontColor;

    ctx.textAlign = "center";
    ctx.font = options.fontSize + " " + options.fontFamily;
    ctx.fillText(
      chartLabel.split("&")[0],
      width / 2 + 50,
      top - 10 + height / 2
    );
    ctx.fillText(
      chartLabel.split("&")[1],
      width / 2 + 50,
      top + 10 + height / 2
    );
  },
};

var outsideLabels = {
  id: "outsideLabels",
  afterDraw(chart, args, options) {
    const {
      ctx,
      chartArea: { top, right, bottom, left, width, height },
    } = chart;

    chart.data.datasets.forEach((dataset, i) => {
      chart.getDatasetMeta(i).data.forEach((datapoint, index) => {
        const { x, y } = datapoint.tooltipPosition();

        // text
        const halfWidth = width / 2;
        const halfHeight = height / 2;

        var xLine = x >= halfWidth ? x + 15 : x - 15;
        var yLine = y >= halfHeight ? y + 15 : y - 15;

        yLine = x == 176.8 ? yLine - 20 : yLine;

        ctx.font = options.fontSize + " " + options.fontFamily;

        var textXPosition = x >= halfWidth ? "left" : "right";
        textXPosition = x == 176.8 ? "center" : textXPosition;

        var extraX = x >= halfWidth ? 20 : -10;
        extraX = x == 176.8 ? -15 : extraX;
        extraX = x == 282.06929373226785 ? 10 : extraX;

        var extraY = y >= halfHeight ? 0 : 0;

        extraY = x == 176.8 ? -15 : extraY;
        extraY = x == 282.06929373226785 ? -45 : extraY;

        ctx.textAlign = textXPosition;
        ctx.textBaseLine = "middle";

        ctx.fillText(chart.data.labels[index], xLine + extraX, yLine + extraY);

        // percentage

        extraX = x >= halfWidth ? 40 : -35;
        extraX = x == 176.8 ? -10 : extraX;
        extraX = x == 282.06929373226785 ? 25 : extraX;

        extraY = y >= halfHeight ? 35 : 5;
        extraY = x == 176.8 ? -27 : extraY;
        extraY = x == 282.06929373226785 ? -10 : extraY;

        ctx.textAlign = "center";
        ctx.textBaseLine = "middle";

        ctx.fillText(
          chart.data.datasets[0].data[index] + "%",
          xLine + extraX,
          y + extraY
        );
      });
    });
  },
};

// chart options
var doughnutChartConfig = {
  type: "doughnut",
  data,
  options: {
    layout: {
      padding: 50,
    },
    plugins: {
      tooltip: {
        enabled: false, // <-- this option disables tooltips
      },
      legend: {
        display: false,
        labels: {
          font: {
            family: "Poppins",
            size: 15,
          },
        },
      },
      insideLabel: {
        fontColor: "#2F3A48",
        fontSize: "15px",
        fontFamily: "Poppins",
      },
    },
  },
  plugins: [insideLabel, outsideLabels],
};

// chart init
var doughnutChart = new Chart(
  document.getElementById("myDoughnutChart"),
  doughnutChartConfig
);

// chart 2
var chartLabel2 = "Tous les métiers";

data = {
  labels: ["Services publics", "CDD", "CDI", "Interim"],
  datasets: [
    {
      chartLabel2,
      data: [2, 9, 28, 61],
      backgroundColor: [colors[0], colors[1], colors[2], colors[3]],
      borderColor: [
        colorBorders[0],
        colorBorders[1],
        colorBorders[2],
        colorBorders[3],
      ],
      hoverOffset: 0,
      cutout: "80%",
    },
  ],
};

// plugins
insideLabel = {
  id: "insideLabel",
  beforeDraw(chart, args, options) {
    const {
      ctx,
      chartArea: { top, right, bottom, left, width, height },
    } = chart;

    ctx.save();

    ctx.fillStyle = options.fontColor;

    ctx.textAlign = "center";
    ctx.font = options.fontSize + " " + options.fontFamily;
    ctx.fillText(chartLabel2, width / 2 + 50, top + height / 2);
  },
};

outsideLabels = {
  id: "outsideLabels",
  afterDraw(chart, args, options) {
    const {
      ctx,
      chartArea: { top, right, bottom, left, width, height },
    } = chart;

    chart.data.datasets.forEach((dataset, i) => {
      chart.getDatasetMeta(i).data.forEach((datapoint, index) => {
        const { x, y } = datapoint.tooltipPosition();

        // text
        const halfWidth = width / 2;
        const halfHeight = height / 2;

        var xLine = x >= halfWidth ? x + 15 : x - 15;
        var yLine = y >= halfHeight ? y + 15 : y - 15;

        var extraX = x >= halfWidth ? 20 : 20;
        extraX = x == 183.90914262110888 ? -80 : extraX;
        extraX = x == 221.76508417766988 ? 20 : extraX;
        extraX = x == 290.02000000000004 ? 0 : extraX;

        var extraY = y >= halfHeight ? 45 : -5;
        extraY = x == 183.90914262110888 ? -35 : extraY;
        extraY = x == 221.76508417766988 ? 0 : extraY;
        extraY = x == 290.02000000000004 ? -90 : extraY;

        ctx.font = options.fontSize + " " + options.fontFamily;

        var textXPosition = x >= halfWidth ? "left" : "right";
        textXPosition = x == 176.8 ? "center" : textXPosition;

        ctx.textAlign = textXPosition;
        ctx.textBaseLine = "middle";

        ctx.fillText(chart.data.labels[index], xLine + extraX, yLine + extraY);

        // percentage

        extraX = x == 183.90914262110888 ? -10 : extraX;
        extraX = x == 221.76508417766988 ? 42 : extraX;
        extraX = x == 290.02000000000004 ? 17 : extraX;
        extraX = x == 70.27347933900259 ? 0 : extraX;

        extraY = x == 183.90914262110888 ? -27 : extraY;
        extraY = x == 221.76508417766988 ? 5 : extraY;
        extraY = x == 290.02000000000004 ? -55 : extraY;
        extraY = x == 70.27347933900259 ? 80 : extraY;

        ctx.textAlign = "center";
        ctx.textBaseLine = "middle";

        ctx.fillText(
          chart.data.datasets[0].data[index] + "%",
          xLine + extraX,
          y + extraY
        );
      });
    });
  },
};

doughnutChartConfig = {
  type: "doughnut",
  data,
  options: {
    layout: {
      padding: 50,
    },
    plugins: {
      legend: {
        display: false,
        labels: {
          font: {
            family: "Poppins",
            size: 15,
          },
        },
      },
      tooltip: {
        enabled: false, // <-- this option disables tooltips
      },
      insideLabel: {
        fontColor: "#2F3A48",
        fontSize: "15px",
        fontFamily: "Poppins",
      },
    },
  },
  plugins: [insideLabel, outsideLabels],
};

var doughnutChart2 = new Chart(
  document.getElementById("myDoughnutChart2"),
  doughnutChartConfig
);

Chart.defaults.font.family = "Poppins";
Chart.defaults.font.size = 15;

// Chart details

var detailsBtn = document.getElementById(
  "accordion-2098845c33-item-ace86c638b-button"
);

var detailsCloseBtn = document.getElementById("close-container");

detailsCloseBtn.addEventListener("click", () => {
  detailsBtn.click();
});
