<template>
	<div>
		<Card>
			<p slot="title">
				权限管理
			</p>
			<Row>
				<Col span="5">
				<iSelect style="width: 90%" v-model="selectRoleModel" @on-change="roleChange">
					<i-option v-for="item in roleList" :value="item.roleCode">{{ item.roleName }}
					</i-option>
				</iSelect>
				</Col>
				<Col span="2">
				<Button type="primary" icon="ios-search" @click="saveAuth">保存</Button>
				</Col>
			</Row>
		</Card>
		<br>
		<Card>
			<Row>

				<Tree :data="baseData" ref='tree' v-model="treeModel" show-checkbox
				      @on-check-change="changeTreeNode"></Tree>

			</Row>
		</Card>

	</div>
</template>
<script>
	import util from '../../libs/util';
	export default {
		data() {
			return {
				self: this,
				treeModel: true,
				selectRoleModel: '',
				formatAuthData: {},
				roleSel: '',
				roleList: [],
				selTreeNodes: [],
				baseData: [],
				treeTempData: [],
				userName: ''
			};
		},
		methods: {
			saveAuth: function () {
				if (this.roleSel == '') {
					this.$Message.error('未选择角色！');
					return;
				}
				let postData = [];
				if (this.selTreeNodes.length > 0) {
					for (let selTreeIndex in this.selTreeNodes) {
						let selNode = this.selTreeNodes[selTreeIndex];
						let tempData = {};
						tempData.permissionId = selNode.id;
						tempData.roleCode = this.roleSel;
						postData[postData.length] = tempData;
					}
				} else {
					postData[postData.length] = {'permissionId': -1, 'roleCode': this.roleSel};
				}
				util.ajax.post(util.basepath() + '/admin/adminrolepermission/batch',
					JSON.stringify(postData)).then(
					response => {
						let savers = response.data;
						if (savers.success) {
							this.initAuth();
							this.$Message.success('保存成功！');
							util.ajax.get(
								util.basepath()
								+ '/admin/adminpermission?limit=2147483647&begin=0').then(
								res => {
									let rsData = res.data;
									this.baseData = [];
									setTimeout(() => {
										this.createTreeNode(rsData);
									}, 500);
								});
						} else {
							this.$Message.error('保存失败!');
						}
					});
			},
			changeTreeNode: function (nodes) {
				this.selTreeNodes = nodes;
			},
			roleChange: function (roleVal) {
				this.roleSel = roleVal;
				util.ajax.get(
					util.basepath() + '/admin/adminpermission?limit=2147483647&begin=0').then(
					res => {
						let rsData = res.data;
						this.baseData = [];
						setTimeout(() => {
							this.createTreeNode(rsData);
						}, 500);
					});
			},
			createTreeNode: function (nodeData) {
				this.treeTempData = [];
				this.selTreeNodes = [];
				let tempData1 = [];
				for (let i in nodeData) {
					let leafItem = nodeData[i];
					let node = {};
					node.expand = false;
					node.title = leafItem.name;
					node.id = leafItem.id;
					let isExists = false;
					node.checked = false;
					if (this.roleSel != '') {
						let authData = this.formatAuthData[this.roleSel];
						for (let authIndex in authData) {
							let auth = authData[authIndex];
							if (auth.permissionId == leafItem.id) {
								node.checked = true;
								node.expand = true;
								this.selTreeNodes.push(node);
							}
						}
					}

					if (leafItem.leaf != 0) {
						for (let parentIndex in this.treeTempData) {
							let parentNode = this.treeTempData[parentIndex];
							if (parentNode.id == leafItem.pid) {
								isExists = true;
								let childNodes = this.treeTempData[parentIndex].children;
								if (childNodes == undefined) {
									childNodes = [];
									childNodes[0] = node;
								} else {
									childNodes[childNodes.length] = node;
								}
								this.treeTempData[parentIndex].children = childNodes;
							}
						}
						if (!isExists) {
							tempData1[tempData1.length] = leafItem;
						} else {
							this.baseData[this.treeTempData.length] = node;
						}
					} else {
						this.treeTempData[this.treeTempData.length] = node;
					}
				}
				this.baseData = this.treeTempData;
				if (tempData1.length == 0) {
					return;
				} else {
					this.createTreeNode(tempData1);
				}
			},
			initAuth: function () {
				this.formatAuthData = {};
				util.ajax.get(
					util.basepath() + '/admin/adminrolepermission?limit=2147483647&begin=0').then(
					res => {
						let rsData = res.data;
						for (let rowIndex in rsData) {
							let row = rsData[rowIndex];
							let tempdata = [];
							if (this.formatAuthData[row.roleCode] != undefined
								&& this.formatAuthData[row.roleCode] != 'undefined') {
								tempdata = this.formatAuthData[row.roleCode];
							}
							tempdata[tempdata.length] = row;
							this.formatAuthData[row.roleCode] = tempdata;
						}
					});
			}

		},
		created: function () {
			this.initAuth();
			util.ajax.get(
				util.basepath() + '/admin/adminpermission?limit=2147483647&begin=0').then(
				res => {
					let rsData = res.data;
					this.baseData = [];
					this.createTreeNode(rsData);
				});
			util.ajax.get(
				util.basepath() + '/admin/adminrole?limit=2147483647&begin=0&').then(
				roles => {
					this.roleList = roles.data;
				});
		}

	};
</script>