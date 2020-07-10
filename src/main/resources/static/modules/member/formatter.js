function memberCmdFormatter(val, row, idx) {
    return [
        '<a class="like" href="javascript:void(0)" title="Like">',
        '<i class="fal fa-edit"></i>',
        '</a>',

        '<a class="remove" href="javascript:void(0)" title="Remove">',
        '<i class="fal fa-trash-alt ml-2"></i>',
        '</a>'
    ].join('')
}

function memberUsernameFormatter(val, row, idx) {
    if (! row.enabled) {
        return '<span class="txt-color-red text-muted">' + val + '</span>';
    }
    return val;
}
