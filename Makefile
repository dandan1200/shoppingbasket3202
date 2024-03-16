run:
	gradle bootRun

build:
	gradle build


clean:
	gradle clean

view:
	curl -s -X GET http://localhost:8080/api/shop/viewall | jq .

# view-all-users: #1
#		curl -s -H "Content-Type: application/json" -X GET http://localhost:8080/api/shop/users | jq .
#
# view-all-costs: #2
# curl -s -H "Content-Type: application/json" -X GET http://localhost:8080/api/shop/costs | jq .
#
# view-user-basket: #3
# 	curl -s -H "Content-Type: application/json" -X GET http://localhost:8080/api/shop/users/userA | jq .
#
# view-user-basket-total: #4
# 	curl -s -H "Content-Type: application/json" -X GET http://localhost:8080/api/shop/users/userA/total | jq .
#
# add-cost: #5
# 	curl -s -H "Content-Type: application/json" -X POST -d '{"name":"papaya", "cost":"1.0"}' http://localhost:8080/api/shop/costs | jq .
#
# modify-cost: #6
# 	curl -s -H "Content-Type: application/json" -X PUT -d '{"name":"papaya", "cost":"2.0"}' http://localhost:8080/api/shop/costs/papaya | jq .
#
# addto-basket: #7
# 	curl -s -H "Content-Type: application/json" -X POST -d '{"name":"papaya", "count":"100"}' http://localhost:8080/api/shop/users/userA/add | jq .
#
# edit-basket: #8
# 	curl -s -H "Content-Type: application/json" -X PUT -d '{"count":"2"}' http://localhost:8080/api/shop/users/userA/basket/papaya | jq .
#
# delete-basket: #9
# 	curl -s -H "Content-Type: application/json" -X DELETE http://localhost:8080/api/shop/users/userA | jq .
# 
# delete-item: 10
#		curl -s -H "Content-Type: application/json" -X DELETE http://localhost:8080/api/shop/users/userA/basket/papaya | jq .
