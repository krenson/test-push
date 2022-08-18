function rand(min, max) {
  min = min || 0;
  max = max || 0;
  _seed = 1;
  _seed = (_seed * 9301 + 49297) % 233280;
  return min + (_seed / 233280) * (max - min);
}

function numbers(config) {
  var cfg = config || {};
  var min = cfg.min || 0;
  var max = cfg.max || 100;
  var from = cfg.from || [];
  var count = cfg.count || 8;
  var decimals = cfg.decimals || 8;
  var continuity = cfg.continuity || 1;
  var dfactor = Math.pow(10, decimals) || 0;
  var data = [];
  var i, value;

  for (i = 0; i < count; ++i) {
    value = (from[i] || 0) + this.rand(min, max);
    if (this.rand() <= continuity) {
      data.push(Math.round(dfactor * value) / dfactor);
    } else {
      data.push(null);
    }
  }

  return data;
}

function points(config) {
  var xs = this.numbers(config);
  var ys = this.numbers(config);
  return xs.map((x, i) => ({ x, y: ys[i] }));
}

function bubbles(config) {
  return this.points(config).map((pt) => {
    pt.r = this.rand(config.rmin, config.rmax);
    return pt;
  });
}

function theLabels(config) {
  var cfg = config || {};
  var min = cfg.min || 0;
  var max = cfg.max || 100;
  var count = cfg.count || 8;
  var step = (max - min) / count;
  var decimals = cfg.decimals || 8;
  var dfactor = Math.pow(10, decimals) || 0;
  var prefix = cfg.prefix || "";
  var values = [];
  var i;

  for (i = min; i < max; i += step) {
    values.push(prefix + Math.round(dfactor * i) / dfactor);
  }

  return values;
}

var MONTHS = [
  "January",
  "February",
  "March",
  "April",
  "May",
  "June",
  "July",
  "August",
  "September",
  "October",
  "November",
  "December",
];

function getMonths(count) {
  var values = [];

  for (i = 0; i < count; ++i) {
    values.push(MONTHS[i]);
  }

  return values;
}

var COLORS = [
  "#4dc9f6",
  "#f67019",
  "#f53794",
  "#537bc4",
  "#acc236",
  "#166a8f",
  "#00a950",
  "#58595b",
  "#8549ba",
];

function color(index) {
  return COLORS[index % COLORS.length];
}

function transparentize(value, opacity) {
  var alpha = opacity === undefined ? 0.5 : 1 - opacity;
  return colorLib(value).alpha(alpha).rgbString();
}

var CHART_COLORS = {
  red: "rgb(255, 99, 132)",
  orange: "rgb(255, 159, 64)",
  yellow: "rgb(255, 205, 86)",
  green: "rgb(75, 192, 192)",
  blue: "rgb(54, 162, 235)",
  purple: "rgb(153, 102, 255)",
  grey: "rgb(201, 203, 207)",
};

var NAMED_COLORS = [
  CHART_COLORS.red,
  CHART_COLORS.orange,
  CHART_COLORS.yellow,
  CHART_COLORS.green,
  CHART_COLORS.blue,
  CHART_COLORS.purple,
  CHART_COLORS.grey,
];

function namedColor(index) {
  return NAMED_COLORS[index % NAMED_COLORS.length];
}

function newDate(days) {
  return DateTime.now().plus({ days }).toJSDate();
}

function newDateString(days) {
  return DateTime.now().plus({ days }).toISO();
}

function parseISODate(str) {
  return DateTime.fromISO(str);
}

var ctx2 = document.getElementById("myChart");

var DATA_COUNT = 4;
var NUMBER_CFG = { count: DATA_COUNT, min: 0, max: 100 };

var labels = getMonths(4);

var data = {
  labels: labels,
  datasets: [
    {
      label: "Dataset 1",
      data: numbers(NUMBER_CFG),
      borderColor: "#2F3A48",
      backgroundColor: "#2F3A48",
    },
    {
      label: "Dataset 2",
      data: numbers(NUMBER_CFG),
      borderColor: "#FBE7CB",
      backgroundColor: "#FBE7CB",
    },
    {
      label: "Dataset 3",
      data: numbers(NUMBER_CFG),
      borderColor: "#FF8E97",
      backgroundColor: "#FF8E97",
    },
    {
      label: "Dataset 4",
      data: numbers(NUMBER_CFG),
      borderColor: "#2F3A48",
      borderWidth: 3,
      backgroundColor: "#FFFFFF",
    },
    {
      label: "Dataset 5",
      data: numbers(NUMBER_CFG),
      borderColor: "#DEC7E0",
      backgroundColor: "#DEC7E0",
    },
    {
      label: "Dataset 6",
      data: numbers(NUMBER_CFG),
      borderColor: "#969CA3",
      backgroundColor: "#969CA3",
    },
    {
      label: "Dataset 7",
      data: numbers(NUMBER_CFG),
      borderColor: "#2F3A48",
      borderColor: 3,
      backgroundColor: "#FCDBAD",
    },
  ],
};

var myChart = new Chart(ctx2, {
  type: "bar",
  data: data,
  options: {
    indexAxis: "y",
    scales: {
      y: {
        beginAtZero: true,
      },
    },
  },
});
