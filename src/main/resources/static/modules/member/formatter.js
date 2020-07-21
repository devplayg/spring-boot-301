function memberUsernameFormatter(val, row, idx) {
    if (! row.enabled) {
        return '<span class="text-muted">' + val + '</span>';
    }
    return val;
}

function memberActionFormatter(val, row, idx) {
    return commonActionFormatter(val, row, idx)
        + ' <a class="password" href="javascript:void(0)" title="Change password"><i class="fal fa-key"></i></a>';
}
