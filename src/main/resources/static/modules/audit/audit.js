$(function() {
    new Pager({
        id: "audit",
        api: "audits",
        showLoading: true,
        rules: {
            ip: {
                required: false,
                ipv4_cidr: true,
            },
        },
    }).run();

    $("select[name=categoryList]").selectpicker();
});
