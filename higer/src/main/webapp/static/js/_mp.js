/**
 * mp公共js
 */

(function (global, $, undefined) {
    global.g_pagenumber = 0;
    global.g_pagerow = 0;
    global._mp = {
        constant: {
            ctx: "http://localhost:8080/lhzsmm",
            success: "success",
            warn: "请选择一条记录",
            delWarn: "请选择要删除的记录"
        },
        cache: {
            data: {},
            clear: function () {
                this.data = {};
            },
            put: function (key, value) {
                this.data[key] = value;
            },
            get: function (key) {
                return this.data[key];
            }
        },
        getPageData: function (parentId, url, page, condtion, callBack) {
            $.ajax({
                url: url + "?pages=" + page + condtion,
                type: "Get",
                dataType: "json",
                success: function (data) {
                    g_pageNumber = data.pageInfo.number;//当前页码
                    g_pagerow = data.pageInfo.size;//行数
                    var datas = data.datas;
                    if (datas) {
                        callBack.apply(this, [datas]);
                    }
                    var pageInfo = data.pageInfo;
                    if (pageInfo) {
                        var pageHtml = "";
                        pageHtml += " <div class='admpage'>";
                        pageHtml += "<ul class='pagination'>";
                        pageHtml += "<li><span style='font-size: 8px;'>当前第"
                            + (parseInt(pageInfo.number) + 1)
                            + "页</span></li>";
                        pageHtml += "<li><a href='javascript:void' onclick='goToPage(1)' id='aFirstPage'>首页</a></li>";
                        var prePage = parseInt(pageInfo.number) == 0 ? 1
                            : parseInt(pageInfo.number);
                        pageHtml += "<li><a href='javascript:void' onclick='goToPage("
                            + prePage + ")' >上一页</a></li>";
                        var nextPage = parseInt(pageInfo.number) + 1 == parseInt(pageInfo.totalPages) ? parseInt(pageInfo.totalPages)
                            : parseInt(pageInfo.number) + 2;
                        pageHtml += "<li><a href='javascript:void' onclick='goToPage("
                            + nextPage + ")' >下一页</a></li>";
                        pageHtml += "<li><a href='javascript:void' onclick='goToPage("
                            + pageInfo.totalPages
                            + ")' >末页</a></li>";
                        pageHtml += "<li><span style='font-size: 8px;'>总共"
                            + (parseInt(pageInfo.totalPages))
                            + "页</span></li>";
                        pageHtml += "<li><span style='font-size: 8px;' >总共"
                            + (parseInt(pageInfo.totalElements))
                            + "条</span></li>";
                        pageHtml += "<li><span>跳转第&nbsp;<input value=" + (pageInfo.number + 1) + " style='width: 35px; height: 17px; line-height: 17px;' type='text'  onchange='goToPage(this.value>" + pageInfo.totalPages + "?" + (pageInfo.totalPages) + ":this.value)'>页</span>" +
                            "</li>";
                        pageHtml += "</ul>";
                        pageHtml += "</div>";
                        // console.log(pageHtml);
                        var pageNode = $(pageHtml);
                        $("#" + parentId).children('div .admpage')
                            .remove();
                        $("#" + parentId).append(pageNode);
                    }
                },
                error: function (XMLHttpRequest, textStatus,
                                 errorThrown) {
                    alert("网络异常");
                }
            });
        },
        getPageData1: function (parentId, url, page, condtion, callBack) {
            console.log(url);
            $.ajax({
                url: url + "?pages=" + page + condtion,
                type: "Get",
                dataType: "json",
                success: function (data) {
                    var datas = data.datas;
                    if (datas) {
                        callBack.apply(this, [datas]);
                    }
                    var pageInfo = data.pageInfo;
                    if (pageInfo) {
                        var pageHtml = "";
                        pageHtml += " <div class='admpage'>";
                        pageHtml += "<ul class='pagination'>";
                        pageHtml += "<li><span style='font-size: 8px;'>当前第"
                            + (parseInt(pageInfo.number) + 1)
                            + "页</span></li>";
                        pageHtml += "<li><a href='javascript:void' onclick='goToPage1(1)' id='aFirstPage'>首页</a></li>";
                        var prePage = parseInt(pageInfo.number) == 0 ? 1
                            : parseInt(pageInfo.number);
                        pageHtml += "<li><a href='javascript:void' onclick='goToPage1("
                            + prePage + ")' >上一页</a></li>";
                        var nextPage = parseInt(pageInfo.number) + 1 == parseInt(pageInfo.totalPages) ? parseInt(pageInfo.totalPages)
                            : parseInt(pageInfo.number) + 2;
                        pageHtml += "<li><a href='javascript:void' onclick='goToPage1("
                            + nextPage + ")' >下一页</a></li>";
                        pageHtml += "<li><a href='javascript:void' onclick='goToPage1("
                            + pageInfo.totalPages
                            + ")' >末页</a></li>";
                        pageHtml += "<li><span style='font-size: 8px;'>总共"
                            + (parseInt(pageInfo.totalPages))
                            + "页</span></li>";
                        pageHtml += "<li><span style='font-size: 8px;' >总共"
                            + (parseInt(pageInfo.totalElements))
                            + "条</span></li>";
                        pageHtml += "</ul>";
                        pageHtml += "</div>";
                        // console.log(pageHtml);
                        var pageNode = $(pageHtml);
                        $("#" + parentId).children('div .admpage')
                            .remove();
                        $("#" + parentId).append(pageNode);
                    }
                },
                error: function (XMLHttpRequest, textStatus,
                                 errorThrown) {
                    alert("网络异常");
                }
            });
        },
        bindFormData: function (formId, data) {
            var inputs = $('#' + formId).find(':input');
            for (var i = 0; i < inputs.length; i++) {
                if (inputs[i].type == 'text') {
                    inputs[i].value = data[inputs[i].name] || "";
                }
                if (inputs[i].type == 'select-one') {
                    var val = data[inputs[i].name];
                    if (!val && val != 0) {
                        $(inputs[i]).val(val || "");
                    } else {
                        $(inputs[i]).val(val || 0);
                    }

                }
                if (inputs[i].type == 'checkbox') {
                    $(inputs[i]).val(data[inputs[i].name] || "");
                }
            }
        },
        bindFormData1: function (formId, data) {
            var inputs = $('#' + formId).find(':input');
            for (var i = 0; i < inputs.length; i++) {
                alert(1);
                var xb = inputs[i].name.split('.');
                alert(xb);
                alert(xb[0]);
                alert(xb[1]);
                if (inputs[i].type == 'text') {
                    inputs[i].value = data[xb[0]][xb[1]];
                }
                if (inputs[i].type == 'select-one') {
                    $(inputs[i]).val(data[xb[0]][xb[1]]);
                }
                if (inputs[i].type == 'checkbox') {
                    $(inputs[i]).val(data[xb[0]][xb[1]]);
                }
            }
        },
        clearFormData: function (formId) {
            var inputs = $('#' + formId).find(':input');
            for (var i = 0; i < inputs.length; i++) {
                if (inputs[i].type == 'text') {
                    inputs[i].value = "";
                }
                if (inputs[i].type == 'select-one') {
                    $(inputs[i]).val("");
                }
            }
        }
    };

})(window, jQuery);