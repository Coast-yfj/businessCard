
var prefix = "/ifast/user"
$(function() {
	load();
});

function load() {
	$('#exampleTable')
			.bootstrapTable(
					{
						method : 'get', // 服务器数据的请求方式 get or post
						url : prefix + "/list", // 服务器数据的加载地址
					//	showRefresh : true,
					//	showToggle : true,
					//	showColumns : true,
						iconSize : 'outline',
						toolbar : '#exampleToolbar',
						striped : true, // 设置为true会有隔行变色效果
						dataType : "json", // 服务器返回的数据类型
						pagination : true, // 设置为true会在底部显示分页条
						singleSelect : false, // 设置为true将禁止多选
						// contentType : "application/x-www-form-urlencoded",
						// //发送到服务器的数据编码类型
						pageSize : 10, // 如果设置了分页，每页数据条数
						pageNumber : 1, // 如果设置了分布，首页页码
						//search : true, // 是否显示搜索框
						showColumns : false, // 是否显示内容下拉框（选择显示的列）
						sidePagination : "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"
						queryParamsType : "",
						// //设置为limit则会发送符合RESTFull格式的参数
						queryParams : function(params) {
							return {
								//说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
							     pageNumber : params.pageNumber,
								 pageSize : params.pageSize  ,
					             name:$('#searchName').val(),
								zt:$("#zt").val()
					           // username:$('#searchName').val()
							};
						},
						// //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
						// queryParamsType = 'limit' ,返回参数必须包含
						// limit, offset, search, sort, order 否则, 需要包含:
						// pageSize, pageNumber, searchText, sortName,
						// sortOrder.
						// 返回false将会终止请求
						responseHandler : function(res){
                            console.log(res);
                            return {
                                "total": res.data.total,//总数
                                "rows": res.data.records   //数据
                             };
                        },
						columns : [
								{
									checkbox : true
								},
																{
									field : 'id', 
									title : '主键' ,
                                    formatter:function(value,row,index){
										return index+1;
                                                                    }
								},
																{
									field : 'name', 
									title : '姓名' ,
									formatter:function (value, row, index) {
                                        var s = "<p style='width: 80px;word-break:break-all'>"+value+"</p>";
                                        return s;
                                    }
								},{
							field:'avatarUrl',
							title:'头像', formatter: function (value, row, index) {
                                    var s;
                                    if (row.avatarUrl != null) {
                                        var url = row.avatarUrl;
                                        s = '<a class = "view"  href="javascript:void(0)"><img style="width:40px;height:40px;"  src="' + url + '" /></a>';
                                    }
                                    return s;
                                },
                                //定义点击之后放大图片的事件
                                events: 'operateEvents'
							},
																{
									field : 'unionid', 
									title : '公司名称' ,
                                     formatter: function (value, row, index) {
									    var s
										if(row.unitDO!=null){
                                             s = "<p style='width: 200px;word-break:break-all'>"+row.unitDO.name+"</p>";
                                            return s;
                                        }else {
											return "";
										}
                                      }
								},{
                                field : 'scale',
                                title : '公司规模' ,
                                formatter: function (value, row, index) {
                                    if(row.unitDO!=null){
                                        return row.unitDO.scale;
                                    }else {
                                        return "";
                                    }
                                }
							},{
                               field:'introduction' ,
								title:'简介',
                                formatter: function (value, row, index) {
                                    if(row.unitDO!=null){
                                        return row.unitDO.introduction;
                                    }else {
                                        return "";
                                    }
                                }
							},
																{
									field : 'position', 
									title : '职位' 
								},
																{
									field : 'phone', 
									title : '电话' 
								},
																{
									field : 'wechat', 
									title : '微信' 
								},
																{
									field : 'qq', 
									title : 'QQ' 
								},
																{
									field : 'email', 
									title : '邮箱' 
								},
																{
									field : 'address', 
									title : '地址' 
								},

																{
									title : '操作',
									field : 'id',
									align : 'center',
									formatter : function(value, row, index) {
                                        var e="";
                                        if(row.unitDO!=null){
                                             e = '<a class="btn btn-primary btn-sm '+s_edit_h+'" href="#" mce_href="#" title="产品信息" onclick="productList(\''
                                                + row.unitDO.id
                                                + '\')"><i class="fa fa-bars"></i></a> ';
                                        }
                                        var a='<a class="btn btn-primary btn-sm '+s_edit_h+'" href="#" mce_href="#" title="编辑" onclick="edit(\''
                                            + row.id
                                            + '\')"><i class="fa fa-edit"></i></a> ';
										var d = '<a class="btn btn-warning btn-sm '+s_remove_h+'" href="#" title="删除"  mce_href="#" onclick="remove(\''
												+ row.id
												+ '\')"><i class="fa fa-remove"></i></a> ';
										var f = '<a class="btn btn-success btn-sm" href="#" title="加入黑名单"  mce_href="#" onclick="addBlackList(\''
												+ row.id
												+ '\')"><i class="fa fa-key"></i></a> ';
										return e+a+d+f;
									}
								} ]
					});
}
function reLoad() {
	$('#exampleTable').bootstrapTable('refresh');
}

function productList(id) {
    layer.open({
        type : 2,
        title : '编辑',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '800px', '520px' ],
        content : '/ifast/product?id=' + id // iframe的url
    });
}

function add() {
	layer.open({
		type : 2,
		title : '增加',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content : prefix + '/add' // iframe的url
	});
}
function edit(id) {
	layer.open({
		type : 2,
		title : '编辑',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content : prefix + '/edit/' + id // iframe的url
	});
}
function remove(id) {
	layer.confirm('确定要删除选中的记录？', {
		btn : [ '确定', '取消' ]
	}, function() {
		$.ajax({
			url : prefix+"/remove",
			type : "post",
			data : {
				'id' : id
			},
			success : function(r) {
				if (r.code==0) {
					layer.msg(r.msg);
					reLoad();
				}else{
					layer.msg(r.msg);
				}
			}
		});
	})
}

function addBlackList(id) {
    layer.confirm('确定加入黑名单？', {
        btn : [ '确定', '取消' ]
    }, function() {
        $.ajax({
            url : prefix+"/addBlackList",
            type : "post",
            data : {
                'id' : id
            },
            success : function(r) {
                if (r.code==0) {
                    layer.msg(r.msg);
                    reLoad();
                }else{
                    layer.msg(r.msg);
                }
            }
        });
    })
}
function batchRemove() {
	var rows = $('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
	if (rows.length == 0) {
		layer.msg("请选择要删除的数据");
		return;
	}
	layer.confirm("确认要删除选中的'" + rows.length + "'条数据吗?", {
		btn : [ '确定', '取消' ]
	// 按钮
	}, function() {
		var ids = new Array();
		// 遍历所有选择的行数据，取每条数据对应的ID
		$.each(rows, function(i, row) {
			ids[i] = row['id'];
		});
		$.ajax({
			type : 'POST',
			data : {
				"ids" : ids
			},
			url : prefix + '/batchRemove',
			success : function(r) {
				if (r.code == 0) {
					layer.msg(r.msg);
					reLoad();
				} else {
					layer.msg(r.msg);
				}
			}
		});
	}, function() {

	});
}

window.operateEvents = {
    'click .view': function (e, value, row, index) {
        var url = row.avatarUrl;
        layer.open({
            type: 1,
            title: false,
            area: ['600px', '500px'],
            skin: 'layui-layer-nobg', //没有背景色
            shadeClose: true,
            content: '<img  style="top:40%;left: 40%; position: absolute;margin-top: -25px;margin-left: -25px;" src="' + url + '"/>'
        });
    }
}