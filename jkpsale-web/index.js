var app = new Vue({
    el: '#app',
    data: { 
        selector: 0,
        goods: [
            {
                items: []
            }
        ],
        total: 0,  // 购买总量
		money: 0,  //购买总金额

    }, 
    methods: {	
        // 食品选购按钮
        increase(index1, index2) {
            this.total++;
            this.goods[index1].items[index2].num++;
			this.money+=this.goods[index1].items[index2].amount

            
			console.log(this.money)
        },
        reduce(index1, index2) {
            if(this.goods[index1].items[index2].num == 0){}else{
                this.total--;
                this.goods[index1].items[index2].num--;
                this.money-=this.goods[index1].items[index2].amount
            }

        },
    } ,
    created(){
        $.ajax({
            url:'jkpsale/productDisplay',
            type:'GET',
            success:(str)=> {
                console.log(str.retObj)
                //var ddd=[]
                for(var i in str.retObj){
                     this.goods[0].items[i] =  {title:str.retObj[i].gameCode,amount:str.retObj[i].prizeAmount,storeNum:str.retObj[i].storeNum,food:str.retObj[i].gameName,details:str.retObj[i].productPara.parameter,img:str.retObj[i].picturePath, num: 0}
                }
                this.$forceUpdate();
                console.log(this.goods[0].items[0]);
            },
            error:function (err){
                alert("失败"+err);
            }
        });
    },
});
