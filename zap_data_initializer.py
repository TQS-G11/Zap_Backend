import requests

URL = "http://localhost:8081"
ZAP_URL = f"{URL}/zap"
API_URL = f"{URL}/api"
PRODUCTS_URL = f"{ZAP_URL}/products"
signup = f"{API_URL}/users/signup"
login_url = f"{API_URL}/users/login"

manager = {
    "username": "caio_costela",
    "password": "amogus123",
    "role": "MANAGER",
    "name": "Caio Costela"
}

client = {
    "username": "lucius",
    "password": "amogus123",
    "role": "CLIENT",
    "name": "Lucius Vinicius"
}

products = [
    {
        "name": "Among Us Pen Drive",
        "img": "https://mir-s3-cdn-cf.behance.net/project_modules/2800_opt_1/7dea57109222637.5fcf37f1395c7.png",
        "description": "A curious pen drive from the classic Among Us Series",
        "quantity": 69,
        "category": "Pen Drive",
        "price": 9.69
    },
    {
        "name": "Cellphone Charger",
        "img": "https://imageio.forbes.com/specials-images/imageserve/1048621702/USB-cable-charger-for-a-smartphone/960x0.jpg?format=jpg&width=960",
        "description": "A charger for a cellphone",
        "quantity": 4,
        "category": "Charger",
        "price": 2.2
    },
    {
        "name": "Sansung Series 9 Notebook Charger / Adapter",
        "img": "https://www.mytrendyphone.eu/images2/Laptop-Charger-Adapter-for-Samsung-Series-5-Series-7-Series-9-Chromebook-Ativ-Book-9-Lite-Plus-40W-18082016-01-p.jpg",
        "description": "A charger for notebook",
        "quantity": 2,
        "category": "Charger",
        "price": 12.0
    }
]


def main():
    x = requests.post(signup, json = manager)
    requests.post(signup, json = client)
    
    logi = requests.post(login_url, json = manager)

    manager_token = logi.json()['token']['token']
    
    headers = {
        "Authorization": f"Bearer {manager_token}"
    }

    for product in products:
        x = requests.post(PRODUCTS_URL, json = product, headers = headers)
        print(x)



if __name__ == "__main__":
    main()