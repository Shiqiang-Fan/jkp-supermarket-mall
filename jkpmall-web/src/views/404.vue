<template>
	<div>
		<div class="area404" id="ha404">
			<div class="area404img"></div>
			<div class="areawords">您访问的页面不存在</div>
			<div class="areabtn">
				<Button @click="goBack" type="info" class="btn btn-biger btn-block btn-primary">
					返回首页
				</Button>
			</div>
		</div>
	</div>
</template>
<script>
	export default {
		methods: {
			goBack: function () {
				this.$router.push({name: 'index'});
			}
		}
	};
</script>
<style>
	/* Button */
	.btn {
		border: none;
		background: #34495e;
		color: white !important;
		font-size: 14px;
		text-decoration: none;
		text-shadow: none;
		text-align: center;
		-webkit-box-shadow: none;
		-moz-box-shadow: none;
		box-shadow: none;
		-webkit-transition: 0.25s;
		-moz-transition: 0.25s;
		-o-transition: 0.25s;
		transition: 0.25s;
		-webkit-backface-visibility: hidden;
		border-radius: 5px;
	}

	.btn:hover, .btn:focus {
		background-color: #4e6d8d;
		color: white;
		-webkit-transition: 0.25s;
		-moz-transition: 0.25s;
		-o-transition: 0.25s;
		transition: 0.25s;
		-webkit-backface-visibility: hidden;
	}

	.btn:active, .btn.active {
		background-color: #2c3e50;
		color: rgba(255, 255, 255, 0.75);
		-webkit-box-shadow: none;
		-moz-box-shadow: none;
		box-shadow: none;
	}

	.btn.btn-biger {
		padding: 20px;
	}

	.btn-block {
		display: block;
		width: 100%;
		padding-right: 0;
		padding-left: 0;
		-webkit-box-sizing: border-box;
		-moz-box-sizing: border-box;
		box-sizing: border-box;
	}

	.btn.btn-cancel {
		background-color: #999999;
	}

	.btn.btn-cancel:hover, .btn.btn-cancel:focus {
		background-color: #6e6e6e;;
	}

	.btn.btn-cancel:active, .btn.btn-cancel.active {
		background-color: #505050;
	}

	/*404*/
	.area404 {
		background: white;
		margin: 2rem auto;
	}

	.area404img {
		width: 17.75rem;
		height: 17.5rem;
		background: white url(../images/404img.jpg) no-repeat 100% 100%;
		background-size: 100%;
		margin: auto;
	}

	.areawords {
		width: 24rem;
		font-size: 20px;
		line-height: 50px;
		color: black;
		text-indent: 160px;
		margin: auto;
	}

	.areabtn {
		width: 16.25rem;
		margin: auto;
		text-align: center;
		padding: 10px 0 30px;
	}

	.areabtn a {
		font-size: 20px;
		text-decoration: none;
	}

	.areabtn a:hover {
		text-decoration: none;
	}
</style>