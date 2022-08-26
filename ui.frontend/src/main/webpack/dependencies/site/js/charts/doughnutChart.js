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

var myDoughnutChart = document.getElementById("myDoughnutChart");

var doughnutChartConfig = {
  type: "doughnut",
  data: {
    labels: ["Autres", "environnement", "Agriculture & pêche"],
    datasets: [
      {
        label: "My First Dataset",
        data: [14, 10, 76],
        backgroundColor: [colors[0], colors[1], colors[2]],
        borderColor: [colorBorders[0], colorBorders[1], colorBorders[2]],
        hoverOffset: 4,
        cutout: "80%",
      },
    ],
  },
  options: {},
};

var doughnutChart = new Chart(myDoughnutChart, doughnutChartConfig);
Chart.defaults.font.family = "Poppins";
Chart.defaults.font.size = 15;
