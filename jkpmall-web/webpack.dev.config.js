const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const ExtractTextPlugin = require('extract-text-webpack-plugin');
const merge = require('webpack-merge');
const webpackBaseConfig = require('./webpack.base.config.js');
const fs = require('fs');

fs.open('./src/config/env.js', 'w', function (err, fd) {
	const buf = 'export default "development";';
	fs.write(fd, buf, "utf-8", function (err, written, buffer){});
});

module.exports = merge(webpackBaseConfig, {
	devtool: '#source-map',
	devServer: {
		historyApiFallback: true,
		hot: true,
		inline: true,
		progress: true,
		port: 8091,
		host: '127.0.0.1',
		proxy: {
			'/jkp/*': {
				target: 'http://localhost:8080/',
				pathRewrite: {'^/jkp': '/'},
				changeOrigin: true,
				secure: false
			}
		}
	},
	output: {
		publicPath: '/dist/dist/',
		filename: '[name].js',
		chunkFilename: '[name].chunk.js'
	},
	plugins: [
		new ExtractTextPlugin({
			filename: '[name].css',
			allChunks: true
		}),
		new webpack.optimize.CommonsChunkPlugin({
			name: 'vendors',
			filename: 'vendors.js'
		}),
		new HtmlWebpackPlugin({
			filename: '../index.html',
			template: './src/template/index.ejs',
			inject: false
		})
	]
});