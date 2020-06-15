function rfc3999PrettyFormatter(val, row, idx) {
    let dt = convertToUserTime(val),
        m = moment(dt);

    return '<span class="txt-color-light">' + m.format("YYYY-MM-DD") + '</span>'
        + ' ' + m.format("HH:mm:ss") + ''
        + '<span class="txt-color-light">' + m.format("Z") + '</span>';
}

function intIpFormatter(val, row, idx) {
    return intToip(val);
}
