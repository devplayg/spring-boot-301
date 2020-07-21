function rfc3999PrettyFormatter(val, row, idx) {
    let dt = convertToUserTime(val),
        m = moment(dt);

    return '<span class="text-soft">' + m.format("YYYY-MM-DD") + '</span>'
        + ' ' + m.format("HH:mm:ss") + ''
        + '<span class="text-soft">' + m.format("Z") + '</span>';
}

function intIpFormatter(val, row, idx) {
    return intToip(val);
}

function commonActionFormatter(val, row, idx) {
    return [
        '<a class="edit" href="javascript:void(0)" title="Update"><i class="fal fa-edit"></i></a>',
        '<a class="delete text-danger" href="javascript:void(0)" title="Delete"><i class="fal fa-trash-alt"></i></a>',
    ].join(' ');
}