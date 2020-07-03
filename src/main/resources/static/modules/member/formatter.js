function memberActionFormatter(val, row, idx) {
    return [
        '<a class="like" href="javascript:void(0)" title="Like">',
        '<i class="fal fa-edit"></i>',
        '</a>',

        '<a class="remove" href="javascript:void(0)" title="Remove">',
        '<i class="fal fa-trash-alt ml-2"></i>',
        '</a>'
    ].join('')
}
