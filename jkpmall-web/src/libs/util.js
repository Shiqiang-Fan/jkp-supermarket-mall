import axios from 'axios';
import env from '../config/env';

let util = {};

util.basepath = function () {
    return '/jkp';
};
util.title = function (title) {
    title = title ? title + ' - Home' : '即开票售彩商城管理后台';
    window.document.title = title;
};

const ajaxUrl = env === 'development' ? 'http://127.0.0.1:8888' : env
=== 'production' ? 'https://www.url.com' : 'https://debug.url.com';

axios.defaults.headers.post['Content-Type'] = 'application/json; charset=UTF-8';
axios.defaults.headers.put['Content-Type'] = 'application/json; charset=UTF-8';

util.ajax = axios.create({
    // baseURL: ajaxUrl,
    timeout: 30000
});
util.equal = function (cname, cvalue) {
    return ('"' + cname + '":"' + cvalue + '"');
};
util.like = function (cname, cvalue) {
    return ('"' + cname + '":"%' + (cvalue) + '%"');
};
util.condition = function (rqls) {
    var ret = JSON.stringify(rqls);
    return ret.substring(1, ret.length - 1);
};
util.and = function (rqls) {
    let ret = '';
    for (let argu in rqls) {
        if (rqls[argu] && rqls[argu].length > 0) {
            if (ret.length > 0) {
                ret += ',';
            }
            ret += rqls[argu];
        }
    }
    return ret;
};
util.drawPie = function (em, chartData, self) {
    var myChart = self.$echarts.init(document.getElementById(em));
    myChart.setOption({
        title: {
            text: chartData.title == undefined ? '' : chartData.title,
            x: chartData.title_x == undefined ? 'center' : chartData.title_x
        },
        tooltip: chartData.tooltip == undefined ? {
            trigger: 'item',
            formatter: '{a} <br/>{b} : {c} ({d}%)'
        } : chartData.tooltip,
        legend: {
            orient: chartData.legend_orient == undefined ? 'vertical'
                : chartData.legend_orient,
            x: chartData.legend_x == undefined ? 'left' : chartData.legend_x,
            data: formatLenArr(chartData.seriesData)
        },
        toolbox: {
            show: chartData.toolbox_show == undefined ? true
                : chartData.toolbox_show,
            feature: chartData.feature == undefined ? {
                dataView: {
                    show: true,
                    readOnly: false
                }, saveAsImage: {show: true, name: 'sava'}
            } : chartData.feature
        },
        calculable: chartData.calculable == undefined ? true
            : chartData.calculable,
        series: [
            {
                name: chartData.seriesName,
                type: 'pie',
                radius: chartData.series_radius == undefined ? '60%'
                    : chartData.series_radius,
                center: chartData.series_center == undefined ? ['50%', '50%']
                    : chartData.series_center,
                itemStyle: {
                    normal: {
                        label: {
                            formatter: '{b}\n{c} ({d}%)'
                        },
                        labelLine: {
                            show: false
                        }
                    }
                },
                data: chartData.seriesData
            }
        ]
    });
};
util.drawLine = function (em, chartData, self) {
    let myChart = self.$echarts.init(document.getElementById(em));
    myChart.setOption({
        title: {
            text: chartData.title
        },
        tooltip: {
            trigger: chartData.tooltip_trigger == undefined ? 'axis'
                : chartData.tooltip_trigger,
            axisPointer: {
                type: chartData.axisPointer_type == undefined ? 'cross'
                    : chartData.axisPointer_type,
                label: {
                    backgroundColor: chartData.label_bk == undefined
                        ? '#FFFfff' : chartData.label_bk
                }
            }
        },
        legend: {
            data: formatLenArr(chartData.seriesData)
        },
        toolbox: {
            feature: {
                saveAsImage: {}
            }
        },
        grid: {
            left: chartData.grid_left == undefined ? '3%'
                : chartData.grid_left,
            right: chartData.grid_right == undefined ? '4%'
                : chartData.grid_right,
            bottom: chartData.grid_bottom == undefined ? '3%'
                : chartData.grid_bottom,
            containLabel: chartData.grid_containLabel == undefined
                ? true : chartData.grid_containLabel
        },
        xAxis: [
            {
                type: 'category',
                boundaryGap: false,
                data: chartData.xAxisData
            }
        ],
        yAxis: [
            {
                type: 'value'
            }
        ],
        series: formatSeries(chartData.seriesData, 'line')
    });
};
util.oneOf = function (ele, targetArr) {
    if (targetArr.indexOf(ele) >= 0) {
        return true;
    } else {
        return false;
    }
};

function formatLenArr(data) {
    var lenArr = [];
    for (var key in data) {
        lenArr[key] = data[key].name;
    }
    return lenArr;
};

function formatSeries(sdata, type) {
    var sArr = [];
    for (var key in sdata) {
        var r = sdata[key];
        var fd = {
            'name': r.name,
            'type': type,
            'stack': r.stack,
            'areaStyle': r.areaStyle == undefined ? {'normal': {}}
                : r.areaStyle,
            'data': r.data
        };
        sArr[key] = fd;
    }
    return sArr;
};
String.prototype.rql = function () {
    return encodeURIComponent('{' + this + '}');
};

util.time2DateStr = function (time) {
    let datetime = '';
    if (time != 0) {
        let d = new Date(time);
        let year = d.getFullYear();
        let month = d.getMonth() + 1;
        if (month < 10) {
            month = `0${month}`;
        }
        var date = d.getDate();
        if (date < 10) {
            date = `0${date}`;
        }
        var hour = d.getHours();
        if (hour < 10) {
            hour = `0${hour}`;
        }
        var minute = d.getMinutes();
        if (minute < 10) {
            minute = `0${minute}`;
        }
        var second = d.getSeconds();
        if (second < 10) {
            second = `0${second}`;
        }
        datetime = `${year}-${month}-${date} ${hour}:${minute}:${second}`;
    }
    return datetime;
};
export default util;